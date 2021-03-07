import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class Config {
	private String path;
	private String imageFolder;
	private String libraryFilePath;
	private String imageSourcePath;
	private int libraryRenewEvery, wallpaperRenewEvery;

	/***** CONSTRUCTOR *****/
	public Config() {
		Path currentPath = Paths.get("");
		path = currentPath.toAbsolutePath().toString()+"\\ChangeMyWallpaperPlz\\";

		loadConfigFile();

		libraryFilePath = path + "library.txt";
		imageSourcePath=path+"imageSource.txt";
	}

	

	/***** METHOD *****/

	public void loadConfigFile() {
		try {
			File f =null;
			
			f=new File(path);
			if(!f.exists()) {
				f.mkdir();
			}
			
			f=new File(path + "config.json");
			if(!f.exists()) {
				createDefaultConfigFile();
			}
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path + "config.json"));

			
			JSONObject jsonObject = (JSONObject) obj;

			this.imageFolder = (String) jsonObject.get("Image Folder");
			this.libraryRenewEvery=Integer.parseInt((String)jsonObject.get("Library Renew Every"));
			this.wallpaperRenewEvery=Integer.parseInt((String)jsonObject.get("Wallpaper Renew Every"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createDefaultConfigFile() {
		JSONObject obj = new JSONObject();
		obj.put("Image Folder", "D:\\ChangeMyWallpaperPlz");
        obj.put("Library Renew Every", "3600000");
        obj.put("Wallpaper Renew Every", "5000");
              
        FileWriter fw=null;
        try {
        	 fw= new FileWriter(path+"config.json");
        	fw.write(obj.toJSONString());
        }catch(Exception e) {
        	e.printStackTrace();
        }finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void offlineMode() {
		libraryFilePath = path + "offLibrary.txt";
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(libraryFilePath));
			File dir =new File(imageFolder);
			String[] extensions = new String[] { "png", "jpg" };
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
			for (File file : files) {
				writer.write(file.getName()+"\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***** GETTER *****/

	public String getImageFolder() {
		return imageFolder;
	}
	
	public String getImageFolderPath() {
		return imageFolder+"\\";
	}

	public String getPath() {
		return path;
	}

	public int getLibraryRenewEvery() {
		return libraryRenewEvery;
	}

	public int getWallpaperRenewEvery() {
		return wallpaperRenewEvery;
	}

	public String getLibraryFilePath() {
		return libraryFilePath;
	}
	
	
	public String getImageSourcePath() {
		return imageSourcePath;
	}
}
