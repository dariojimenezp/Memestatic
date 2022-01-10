package Client;

import GUI.ImageExplorer;
import ServerClientObjects.Post;
import Exceptions.NoServerFoundException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    private static final int IMAGE_MAX_SIZE = 32000000;
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
        //GUI.start();
        //new FeedPage(null);
        Post post = new Post("test1", "dario_jimenezp0107");
        client.publishPost(post, ImageExplorer.getPath());


    }
}
