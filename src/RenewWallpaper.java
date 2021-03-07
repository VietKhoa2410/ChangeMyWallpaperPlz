import java.util.HashMap;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

public class RenewWallpaper extends Thread {
	private UI ui;
	private Config cf;
	private Library library;

	public RenewWallpaper(UI ui, Config cf, Library library) {
		this.ui = ui;
		this.cf = cf;
		this.library = library;
	}

	@Override
	@SuppressWarnings("static-access")
	public void run() {
		String imgLink = new String();
		do {
			try {
				imgLink = library.getRandomImgLink();
				int index=imgLink.indexOf(".it/");
				String imgName=new String();
				if(index!=-1)
					imgName = imgLink.substring(index + 4);
				else 
					imgName=imgLink;
				
				if (!library.isImgStoraged(cf.getImageFolderPath() + imgName))
					library.getImage(imgLink, imgName, cf.getImageFolderPath());
				else
					changeWallpage(cf.getImageFolderPath() + imgName);

				ui.currentImage = imgLink;

			} catch (StringIndexOutOfBoundsException e) {
				e.printStackTrace();
				if (library.getLibrarySet().size() != 0)
					library.removeErrorImage(imgLink);
				else 
					library.renewLibrary();
				
			} finally {
				try {
					this.sleep(cf.getWallpaperRenewEvery());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} while (true);
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

		@SuppressWarnings("deprecation")
		SPI INSTANCE = Native.loadLibrary("user32", SPI.class, new HashMap<String, Object>() {
			
			private static final long serialVersionUID = 1L;

			{
				put(OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
				put(OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
			}
		});

		boolean SystemParametersInfo(UINT_PTR uiAction, UINT_PTR uiParam, String pvParam, UINT_PTR fWinIni);
	}
}
