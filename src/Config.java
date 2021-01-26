import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class Config {
	private String path;
	private String sourceFileName;
	private String sourceFilePath;
	private String storageFolder;
	private String storagePath;
	private Set<String> urlSet = new HashSet<String>();
	private Set<String> imgLinkSet = new HashSet<String>();
	private int sourceFileRenewEvery, wallpaperRenewEvery;

	/***** CONSTRUCTOR *****/
	public Config() {
		Path currentPath = Paths.get("");
		path = currentPath.toAbsolutePath().toString()+"\\ChangeMyWallpaperPlz";

		loadConfigFile();

		storagePath = path + "\\" + storageFolder + "\\";
		sourceFilePath = path + "\\" + sourceFileName + "\\";
	}

	/***** METHOD *****/

	public void loadConfigFile() {
		try {
			File f =null;
			
			f=new File(path);
			if(!f.exists()) {
				f.mkdir();
			}
			
			f=new File(path + "/config.json");
			if(!f.exists()) {
				createDefaultConfigFile();
			}
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path + "/config.json"));

			
			JSONObject jsonObject = (JSONObject) obj;

			this.storageFolder = (String) jsonObject.get("Storage Folder");
			
			this.sourceFileName = (String) jsonObject.get("Source File");
			
			this.sourceFileRenewEvery=Integer.parseInt((String)jsonObject.get("Source File Renew Every"));
			
			this.wallpaperRenewEvery=Integer.parseInt((String)jsonObject.get("Wallpaper Renew Every"));

			// A JSON array. JSONObject supports java.util.List interface
			JSONArray companyList = (JSONArray) jsonObject.get("Reddit URL");

			Iterator<JSONObject> iterator = companyList.iterator();
			while (iterator.hasNext()) {
				Object url = (Object) iterator.next();
				urlSet.add(url.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void createDefaultConfigFile() {
		JSONObject obj = new JSONObject();
		obj.put("Storage Folder", "img");
        obj.put("Source File", "imageLink.txt");
        obj.put("Source File Renew Every", "3600000");
        obj.put("Wallpaper Renew Every", "5000");
        
        JSONArray url = new JSONArray();
        url.add("https://www.reddit.com/r/wallpaper");
        url.add("https://www.reddit.com/r/food");
        obj.put("Reddit URL", url);
        
        FileWriter fw=null;
        try {
        	 fw= new FileWriter(path+"\\config.json");
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

	/***** GETTER *****/

	public String getStorageFolder() {
		return storageFolder;
	}

	public String getPath() {
		return path;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public Set<String> getUrlSet() {
		return urlSet;
	}

	public Set<String> getImgLinkSet() {
		return imgLinkSet;
	}

	public void setImgLinkSet(Set<String> imgSet) {
		this.imgLinkSet = imgSet;
	}

	public int getSourceFileRenewEvery() {
		return sourceFileRenewEvery;
	}

	public int getWallpaperRenewEvery() {
		return wallpaperRenewEvery;
	}

}
