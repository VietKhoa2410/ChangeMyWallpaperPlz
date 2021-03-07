import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ImageSource {
	private Set<String> imageSourceSet = new HashSet<String>();
	private Config cf;

	public ImageSource(Config cf) {
		this.cf = cf;
		loadImageSource();
	}

	public void loadImageSource() {
		imageSourceSet = new HashSet<String>();
		String url = new String();
		try {
			// openfile
			File file = new File(cf.getImageSourcePath());
			if (file.exists()) {
				Scanner sc = new Scanner(file);
				while (sc.hasNextLine()) {
					url = sc.nextLine().replaceAll(" ", "");

					String[] temp = url.split("https://www.reddit.com/r/");
					for (String string : temp) {
						if (string.length() > 0) {
							imageSourceSet.add("https://www.reddit.com/r/" +string);
						}
					}
				}
				sc.close();
			} else {
				createDefaultImageSource();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createDefaultImageSource() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(cf.getPath() + "imageSource.txt"));
			writer.write("https://www.reddit.com/r/wallpaper/");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<String> getImageSourceSet() {
		loadImageSource();
		return imageSourceSet;
	}

	public void setImageSourceSet(Set<String> imageSourceSet) {
		this.imageSourceSet = imageSourceSet;
	}

}
