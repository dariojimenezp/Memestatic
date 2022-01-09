package Client;

import Exceptions.NoServerFoundException;
import GUI.GUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


import javax.imageio.ImageIO;
import javax.swing.*;

import GUI.ImageExplorer;
import GUI.FeedPage;
public class Main extends Application {

    private static final int IMAGE_MAX_SIZE = 32000000;
    public static Client client;

    public static void main(String[] args) {
       launch(args);

    }



    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        /*
        try {
            client = new Client("localhost", 3000);
        } catch (NoServerFoundException e) {
            e.printStackTrace();
        }*/
        //GUI.start();
        new FeedPage(null);


    }
}
