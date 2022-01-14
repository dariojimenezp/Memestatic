package Client;

import GUI.FeedPage;
import GUI.GUI;
import GUI.ImageExplorer;
import ServerClientObjects.Post;
import Exceptions.NoServerFoundException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


//TODO: fix comment box
//TODO: profile
//TODO: test search
public class Main extends Application {

    public static Client client;

    public static void main(String[] args) {
       launch(args);

    }



    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {


        try {
            client = new Client("localhost", 3000);
        } catch (NoServerFoundException e) {
            e.printStackTrace();
        }


        GUI.start();


    }
}


//TODO fix create account
//TODO: log out