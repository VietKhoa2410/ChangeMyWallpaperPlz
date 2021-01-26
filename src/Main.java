
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.UINT_PTR;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

public class Main {
	

	public static void main(String[] args) {
		Config cf = new Config();

		GetAndRenewWallpaper getAndRenewWallpaper=new GetAndRenewWallpaper(cf);
		
		getAndRenewWallpaper.loadImgLink();

		RenewSourceFile renewSourceFile = new RenewSourceFile( cf);
		renewSourceFile.start();
		
		
		while(cf.getImgLinkSet().size()==0) {
			try {
				getAndRenewWallpaper.sleep(5000);
				getAndRenewWallpaper.loadImgLink();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getAndRenewWallpaper.start();
		
		
	}

	
	
	

	
}

