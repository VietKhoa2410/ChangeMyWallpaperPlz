
import java.awt.EventQueue;


public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Config cf = new Config();
				
					ImageSource imageSource = new ImageSource(cf);
					Library library = new Library(cf,imageSource);
					if(!library.isImgStoraged(cf.getPath()+"Icon.png")) {
						library.getImage("https://image.flaticon.com/icons/png/512/49/49398.png", "Icon.png", cf.getPath());
					}
					
					UI ui = new UI(cf,imageSource,library);
					ui.setVisible(true);
					
					library.setUi(ui);
					
					ui.loadConfig();
					ui.loadImageSource();
					ui.loadLibrary();
					
					library.start();
					
					RenewWallpaper renewWallpaper = new RenewWallpaper(ui, cf, library);
					renewWallpaper.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
