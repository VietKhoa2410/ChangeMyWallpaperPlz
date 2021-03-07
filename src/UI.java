import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONObject;

public class UI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbImageFolderPath;
	private JTextField txtfLibraryRenew;
	private JTextField txtfWallpaperRenew;
	private JTextArea txtaImageSource;
	private JTextArea txtaLibrary;
	public String currentImage;
	private ImageSource imageSource;
	private Library library;
	private Config cf;
	private TrayIcon trayIcon;
	private SystemTray tray;

	/*
	 * Create the frame.
	 */
	public UI(Config cf, ImageSource imageSource, Library library) {
		this.cf = cf;
		this.imageSource = imageSource;
		this.library = library;

		setResizable(false);
		setTitle("Change My Wallpaper Plz");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width=1100, height=800;
		double x = (screenSize.getWidth()-width)/2;
		double y = (screenSize.getHeight()-height)/2-20;
		
		setBounds((int)x, (int)y, width, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Unable to set LookAndFeel");
		}
		
		if (SystemTray.isSupported()) {
			System.out.println("system tray supported");
			tray = SystemTray.getSystemTray();

			Image image = Toolkit.getDefaultToolkit()
					.getImage(cf.getPath()+"\\Icon.png");
			ActionListener exitListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			};
			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Exit");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);
			defaultItem = new MenuItem("Open");
			defaultItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(true);
					setExtendedState(JFrame.NORMAL);
				}
			});
			popup.add(defaultItem);
			trayIcon = new TrayIcon(image, "Change My Wallpaper Plz", popup);
			trayIcon.setImageAutoSize(true);
		} else {
			System.out.println("system tray not supported");
		}
		addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				
				// state = 1
				if (e.getNewState() == ICONIFIED) {
					try {
						tray.add(trayIcon);
						setVisible(false);
					} catch (AWTException ex) {
						System.out.println("unable to add to tray");
					}
				}
				// state = 0
				if (e.getNewState() == NORMAL) {
					tray.remove(trayIcon);
					setVisible(true);
				}
			}
		});
		Image image = Toolkit.getDefaultToolkit()
				.getImage("D:\\Code\\Java\\JavaDesktop\\Mine\\ChangeMyWallpaperPlz\\icon.png");

		setIconImage(image);
		
		/********************************/
		
		

		JLabel lbHeader = new JLabel("Change My Wallpaper Please");
		lbHeader.setFont(new Font("Tahoma", Font.PLAIN, 50));
		lbHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lbHeader.setBounds(98, 10, 865, 76);
		contentPane.add(lbHeader);

		JPanel panelConfig = new JPanel();
		panelConfig.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panelConfig.setBounds(40, 111, 990, 101);
		contentPane.add(panelConfig);
		panelConfig.setLayout(null);

		JLabel lbImageFolder = new JLabel("Image Folder:");
		lbImageFolder.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbImageFolder.setBounds(22, 9, 130, 25);
		panelConfig.add(lbImageFolder);

		JButton btnImageFolder = new JButton("Select");
		btnImageFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImageFolder();
			}
		});
		btnImageFolder.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnImageFolder.setBounds(856, 8, 110, 27);
		panelConfig.add(btnImageFolder);

		JLabel lbLibraryRenew = new JLabel("Renew library every(milisecond):");
		lbLibraryRenew.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbLibraryRenew.setBounds(22, 58, 298, 25);
		panelConfig.add(lbLibraryRenew);

		txtfLibraryRenew = new JTextField();
		txtfLibraryRenew.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtfLibraryRenew.setHorizontalAlignment(SwingConstants.CENTER);
		txtfLibraryRenew.setBounds(340, 58, 110, 26);
		panelConfig.add(txtfLibraryRenew);
		txtfLibraryRenew.setColumns(10);

		JLabel lbWallpaperRenew = new JLabel("Renew wallpaper every(milisecond):");
		lbWallpaperRenew.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lbWallpaperRenew.setBounds(523, 59, 325, 23);
		panelConfig.add(lbWallpaperRenew);

		txtfWallpaperRenew = new JTextField();
		txtfWallpaperRenew.setHorizontalAlignment(SwingConstants.CENTER);
		txtfWallpaperRenew.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtfWallpaperRenew.setBounds(856, 58, 110, 26);
		panelConfig.add(txtfWallpaperRenew);
		txtfWallpaperRenew.setColumns(10);

		lbImageFolderPath = new JLabel("");
		lbImageFolderPath.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbImageFolderPath.setHorizontalAlignment(SwingConstants.CENTER);
		lbImageFolderPath.setBounds(162, 9, 684, 25);
		panelConfig.add(lbImageFolderPath);

		JPanel panelImageSource = new JPanel();
		panelImageSource.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panelImageSource.setBounds(40, 260, 450, 408);
		contentPane.add(panelImageSource);
		panelImageSource.setLayout(null);

		JLabel lbImageSource = new JLabel("Image Source");
		lbImageSource.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lbImageSource.setHorizontalAlignment(SwingConstants.CENTER);
		lbImageSource.setBounds(128, 10, 177, 29);
		panelImageSource.add(lbImageSource);

		JScrollPane scrollPaneImageSource = new JScrollPane();
		scrollPaneImageSource.setBounds(10, 68, 430, 325);
		panelImageSource.add(scrollPaneImageSource);

		txtaImageSource = new JTextArea();
		txtaImageSource.setFont(new Font("Tahoma", Font.PLAIN, 18));
		scrollPaneImageSource.setViewportView(txtaImageSource);

		JPanel panelLibrary = new JPanel();
		panelLibrary.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		panelLibrary.setBounds(566, 260, 464, 408);
		contentPane.add(panelLibrary);
		panelLibrary.setLayout(null);

		JLabel lbLibrary = new JLabel("Library");
		lbLibrary.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lbLibrary.setHorizontalAlignment(SwingConstants.CENTER);
		lbLibrary.setBounds(156, 10, 128, 35);
		panelLibrary.add(lbLibrary);

		JScrollPane scrollPaneLibrary = new JScrollPane();
		scrollPaneLibrary.setBounds(10, 68, 444, 325);
		panelLibrary.add(scrollPaneLibrary);

		txtaLibrary = new JTextArea();
		scrollPaneLibrary.setViewportView(txtaLibrary);
		txtaLibrary.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JButton btnCurrentImage = new JButton("Get Image Link");
		btnCurrentImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getCurrentImage();
			}
		});
		btnCurrentImage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnCurrentImage.setBounds(180, 702, 159, 29);
		contentPane.add(btnCurrentImage);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(725, 701, 157, 29);
		contentPane.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 18));
	}

	/*************************************************************************************************************************************************/
	private void selectImageFolder() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setDialogTitle("Select folder to save image");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			lbImageFolderPath.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}

	public void loadConfig() {
		lbImageFolderPath.setText(cf.getImageFolder());
		txtfLibraryRenew.setText(String.valueOf(cf.getLibraryRenewEvery()));
		txtfWallpaperRenew.setText(String.valueOf(cf.getWallpaperRenewEvery()));
	}

	
	public void loadImageSource() {
		String temp = new String();
		Set<String> imageSourceSet = imageSource.getImageSourceSet();
		for (String imageSource : imageSourceSet) {
			temp += imageSource + "\n";
		}
		txtaImageSource.setText(temp);
	}
	
	public void loadLibrary() {
		String temp = new String();
		Set<String> librarySet = library.getLibrarySet();
		for (String imageLink : librarySet) {
			temp += imageLink + "\n";
		}
		txtaLibrary.setText(temp);
	}

	private void getCurrentImage() {
		StringSelection stringSelection = new StringSelection(currentImage);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	private void save() {
		saveConfig();
		saveImageSource();
		saveLibrary();
		cf.loadConfigFile();
		library.loadLibrary();
		imageSource.loadImageSource();
		loadImageSource();
		loadLibrary();
	}

	@SuppressWarnings("unchecked")
	private void saveConfig() {

		JSONObject obj = new JSONObject();
		obj.put("Image Folder", lbImageFolderPath.getText());
		obj.put("Library Renew Every", txtfLibraryRenew.getText());
		obj.put("Wallpaper Renew Every", txtfWallpaperRenew.getText());

		FileWriter fw = null;
		try {
			fw = new FileWriter(cf.getPath() + "\\config.json");
			fw.write(obj.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveImageSource() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(cf.getPath() + "\\imageSource.txt"));
			writer.write(txtaImageSource.getText());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveLibrary() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(cf.getPath() + "\\library.txt"));
			writer.write(txtaLibrary.getText());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
