import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Library extends Thread {
	private Config cf;
	private Set<String> librarySet = new HashSet<String>();
	private ImageSource imageSource;
	private UI ui;
	private boolean online=true;

	public Library(Config cf, ImageSource imageSource) {
		this.cf = cf;
		this.imageSource = imageSource;
		loadLibrary();
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {

		try {
			do {
				if(online)
					renewLibrary();
				ui.loadLibrary();
				this.sleep(cf.getLibraryRenewEvery());
			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// save img link from image source
	public void renewLibrary() {
		String src = new String();

		try {
			// get data from web
			Set<String> imageSourceSet = imageSource.getImageSourceSet();

			for (String url : imageSourceSet) {
				Document doc = Jsoup.connect(url).get();

				// "_2_tDEnGMLxpM6uOa2kaDB3" is Reddit's image class
				Object arrImg[] = doc.getElementsByClass("_2_tDEnGMLxpM6uOa2kaDB3").toArray();

				for (Object object : arrImg) {
					src = object.toString();
					/*
					 * Two type of image url from reddit 
					 * 1. This type used when image uploaded directly to reddit 
					 * https://preview.redd.it/************.jpg?width=.... 
					 * Can't access directly by above link if not change 
					 * https://preview.redd.it to https://i.redd.it
					 * 
					 * 2. This type used when user share image from other web(imgur,...)
					 * https://external-preview.redd.it/********?width=....
					 * 
					 * NOTE: I just get image from type 1
					 */
					
					if(src.indexOf("https://external-preview")!=-1) {

						int start = src.indexOf("https://preview.redd.it/") ;
	
						if (start < 0) {
							continue;
						}
	
						int end = src.indexOf(".jpg");
						if (end == -1)
							end = src.indexOf(".png");
	
						src = "https://i.redd.it/" + src.substring(start+ 24, end+4);
	
						librarySet.add(src);
					}
				}
			}

			Formatter f = new Formatter(cf.getLibraryFilePath());
			for (String link : librarySet) {
				f.format("%s\n", link);
			}
			f.close();
		} catch (Exception e) {
			if(online) {
				online=false;
				cf.offlineMode();
				JOptionPane.showConfirmDialog(ui, "Trying to use your local library", "No Internet Access", JOptionPane.WARNING_MESSAGE);
			}
		} finally {
			loadLibrary();
			ui.loadLibrary();
		}
	}

	// read image link from library.txt
	public void loadLibrary() {
		librarySet.clear();
		File file = null;
		try {
			// openfile
			String url = new String();
			file = new File(cf.getLibraryFilePath());
			if (file.exists()) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					url = sc.nextLine().replaceAll(" ", "");
					//Check if two link connect because of edit error
					String[] temp = url.split("https://i.redd.it/");
					for (String string : temp) {
						if (string.length() > 0) {
							if (string.matches("(.*)((.jpg)|(.png))"))
								librarySet.add("https://i.redd.it/" +string);
						}
					}
				}
				sc.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// check if image locally saved
	public boolean isImgStoraged(String imgPath) {
		File file = new File(imgPath);
		return file.exists();
	}

	// download image
	public void getImage(String imgLink, String imgName, String storagePath) {
		if(online)
			try {
//				System.out.println(imgLink);
				URL url = new URL(imgLink);
	
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
	
				// create storage folder if not exist
				try {
					File storage = new File(storagePath);
					storage.mkdir();
				} catch (Exception e) {
	
				}
	
				FileOutputStream fos = new FileOutputStream(storagePath + imgName);
				fos.write(response);
				fos.close();
			} catch (FileNotFoundException e) {
				librarySet.remove(imgLink);
				e.printStackTrace();
			}catch(UnknownHostException e) {
			}catch(MalformedURLException e) {
				cf.loadConfigFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// get random link from library
	public String getRandomImgLink() {
		if(librarySet.size()==0 && !online) {
			JOptionPane.showMessageDialog(ui, "Can't change your wallpaper because you don't have any image saved locally","Error",JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		int random = (int) (Math.random() * (librarySet.size()));
		String imgLink = "";
		int count = 0;

		for (String string : librarySet) {
			if (count == random) {
				imgLink = string;
				break;
			}
			count++;
		}
		return imgLink;
	}

	public Set<String> getLibrarySet() {
		return librarySet;
	}

	public void setLibrarySet(Set<String> librarySet) {
		this.librarySet = librarySet;
	}

	public void setUi(UI ui) {
		this.ui = ui;
	}

	public void removeErrorImage(String img) {
		librarySet.remove(img);
	}

}
