package Client;

import GUI.GUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import GUI.StartController;

public class Main extends Application {

    public static void main(String[] args) {
       launch(args);

    }



    @Override
    public void start(Stage primaryStage){
        GUI.start();
        //StartController startController = new StartController(GUI.start());
    }
}
