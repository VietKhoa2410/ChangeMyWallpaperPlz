import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RenewSourceFile extends Thread{
	private Set<String> imgLinkSet = new HashSet<String>();
	private Set<String> urlSet;
	private Config cf;

	public RenewSourceFile(Config cf) {

		this.cf = cf;
		this.urlSet = cf.getUrlSet();
		this.imgLinkSet = cf.getImgLinkSet();
	}

	@Override
	public void run() {

		try {
			do {
			renewSourceFile();
			Thread.sleep(cf.getSourceFileRenewEvery());
			}while(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void renewSourceFile() {

		try {
			// get data from web
//			String url = "https://www.reddit.com/r/NoSanaNoLife/?f=flair_name%3A%22Pictures%22";

			for (String url : urlSet) {
				Document doc = Jsoup.connect(url).get();

				Object arrImg[] = doc.getElementsByClass("_2_tDEnGMLxpM6uOa2kaDB3").toArray();

				for (Object object : arrImg) {
					String src = object.toString();

					int start = src.indexOf(".redd.it");

					if (start < 0) {
//						System.out.println("continue");
						continue;
					}
					int end = src.indexOf("?");
					src = "https://i.redd.it/" + src.substring(start + 9, end);

					if (src.length() < 40)
						imgLinkSet.add(src);
				}
			}

			cf.setImgLinkSet(imgLinkSet);

			// save file
			Formatter f = new Formatter(cf.getSourceFilePath());

			for (String link : imgLinkSet) {
				f.format("%s\n", link);
			}
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
