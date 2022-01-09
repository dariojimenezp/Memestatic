package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class ImageExplorer {


    public static String getPath(){
        FileDialog fd = new FileDialog(new JFrame());
        fd.setVisible(true);
        File[] f = fd.getFiles();

        if(f.length > 0){
            return fd.getFiles()[0].getAbsolutePath();
        }

        return null;
    }

    public static void downloadImage(String imageID, String imageType, byte[] imageArray){
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArray));
            ImageIO.write(image, imageType, new File("C:\\Users\\dario\\OneDrive\\Escritorio\\Repos\\Memestatic\\Server\\src\\main\\resources\\Images\\img_" + imageID + "." + imageType));
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static byte[] convertImageToByteArray(String path, String imageType){

        try {
            /* create image file */
            BufferedImage image = ImageIO.read(new File(  path));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, imageType, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        }
        catch (IOException e){ System.out.println(e); }
        return null;
    }

    public static String getImageType(String path){

        String[] directories = path.split("\\\\");
        String imageName = directories[directories.length-1];
        String[] div = imageName.split("\\.");
        return div[1];
    }

    public static Boolean isValidImageType(String imageType){
        return imageType.equals("png") || imageType.equals("jpg") || imageType.equals("jpeg");
    }
}
