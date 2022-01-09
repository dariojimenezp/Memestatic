package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileExplorer {


    public static String getPath(){
        FileDialog fd = new FileDialog(new JFrame());
        fd.setVisible(true);
        File[] f = fd.getFiles();

        if(f.length > 0){
            return fd.getFiles()[0].getAbsolutePath();
        }

        return null;
    }
}
