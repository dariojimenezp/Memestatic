package GUI;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class ImageExplorer {


    public enum Project{
        SERVER,
        CLIENT
    };
    public static String getPath(Stage stage){

        FileChooser chooser = new FileChooser();
        return chooser.showOpenDialog(stage).getAbsolutePath();
    }

    public static void downloadImage(String imageID, String imageType, byte[] imageArray, Project project){
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArray));
            if(project == Project.SERVER) ImageIO.write(image, imageType, new File("src\\main\\resources\\Images\\img_" + imageID + "." + imageType));

            else ImageIO.write(image, imageType, new File("target\\classes\\Images\\img_" + imageID + "." + imageType));


        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static byte[] convertImageToByteArray(String path, String imageType){

        try {
            /* create image file */
            BufferedImage image = ImageIO.read(new File( path));
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
