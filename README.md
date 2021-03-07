# ChangeMyWallpaperPlz
Get image from Reddit, save it local and change your desktop wallpaper randomly

***************************************************************************************************************************************************
* Program config folder will be save at the same folder with file exe. It contain:
  - File config.json save all attribute of this application:
    + Image Folder: The folder to save image. Default is D:/ChangeMyWallpaperPlz
    + Wallpaper Renew Every: How long the wallpaper will be renew(milisecond).
    + Library Renew Every: How long the "Source File" update data.
* Run with Java 8.
* If you have any error when use this application, please report me at: vietkhoa2410@gmail.com

  
  **************************************************************************************************************************************************
  
  Version and Update:
    * 0.1(25/01/2021):
      - It work! :)
      - Have 2 file: "ChangeMyWallpaperPlz.jar" and "ChangeMyWallpaperPlz.exe"
      - The .jar can be use by command line but if you turn off command line, it will turn off too.
      - The .exe can run without any dependency. The only problem is I dont know how to turn off it :) Yes, you can restart laptop/pc to let it down.
      - I will make something to stop the .exe . Other one is I wanna check how many hardware it take and I hope I can reduce it.
    * 1.0(03/03/2021):
      ![image](https://user-images.githubusercontent.com/48951876/110248793-6bcf2100-7fa5-11eb-80a9-d17c4047de84.png)
      - Add user interface.
      - Feature:
        + Can change image folder.
        + Edit image source and image link.
        + Application will load image link from library.txt, get a random link, check is it save locally. If not, it will download it and save to image folder. After that wallpaper will be change.
        + Auto remove invaid image link from library.
        + Hire application icon in notification area.  
