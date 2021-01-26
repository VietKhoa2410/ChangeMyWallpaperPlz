import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;


public class GetAndRenewWallpaper extends Thread{
	private Config cf;
	private Set<String> imgLinkSet = new HashSet<String>();

	public GetAndRenewWallpaper(Config cf) {
		this.cf = cf;
		this.imgLinkSet = cf.getImgLinkSet();
	}

	public void run() {
		try {
		do {
			this.imgLinkSet = cf.getImgLinkSet();
			String imgLink = getRandomImgLink();
			String imgName = imgLink.substring(imgLink.indexOf(".it/") + 4);

			if (!isImgStoraged(cf.getStoragePath() + imgName))
				getImage(imgLink, imgName, cf.getStoragePath());
			else
//				System.out.println("Dont need get img");
			changeWallpage(cf.getStoragePath() + imgName);
			this.sleep(cf.getWallpaperRenewEvery());
		} while (true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loadImgLink() {

		try {
			// openfile
			File file = new File(cf.getSourceFilePath());
			if (file.exists()) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					imgLinkSet.add(sc.nextLine());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		cf.setImgLinkSet(imgLinkSet);
	}

	private String getRandomImgLink() {
		int random = (int) (Math.random() * (imgLinkSet.size()));
//		System.out.println("Random " + random);
		String imgLink = "";
		int count = 0;

		for (String string : imgLinkSet) {
			if (count == random) {
				imgLink = string;
				break;
			}
			count++;
		}
		return imgLink;
	}

	private boolean isImgStoraged(String imgPath) {
		File file = new File(imgPath);
		return file.exists();
	}

	private void getImage(String imgLink, String imgName, String storagePath) {
		try {
			URL url = new URL(imgLink);

			// Get image
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1 != (n = in.read(buf))) {
				out.write(buf, 0, n);
			}
			out.close();
			in.close();

			// Save image
			byte[] response = out.toByteArray();

			
			try {
				File storage = new File(storagePath);
				storage.mkdir();
			}catch(Exception e) {
				
			}

			FileOutputStream fos = new FileOutputStream(storagePath + imgName);
			fos.write(response);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			imgLinkSet.remove(imgLink);
		}
	}

	// change wallpage
	private void changeWallpage(String imgPath) {
		SPI.INSTANCE.SystemParametersInfo(new UINT_PTR(SPI.SPI_SETDESKWALLPAPER), new UINT_PTR(0), imgPath,
				new UINT_PTR(SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE));
	}

	public interface SPI extends StdCallLibrary {

		// from MSDN article
		long SPI_SETDESKWALLPAPER = 20;
		long SPIF_UPDATEINIFILE = 0x01;
		long SPIF_SENDWININICHANGE = 0x02;

		SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class, new HashMap<String, Object>() {
			{
				put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
				put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
			}
		});

		boolean SystemParametersInfo(UINT_PTR uiAction, UINT_PTR uiParam, String pvParam, UINT_PTR fWinIni);
	}
}
