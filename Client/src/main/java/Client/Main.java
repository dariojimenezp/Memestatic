package Client;

import GUI.ImageExplorer;
import ServerClientObjects.Post;
import Exceptions.NoServerFoundException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

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
        //client.getPosts();

        String path = ImageExplorer.getPath();
        Post p = new Post("smort", "djp", ImageExplorer.convertImageToByteArray(path, ImageExplorer.getImageType(path)));
        client.publishPost(p, path);
        ArrayList<Post> posts = client.getPosts();
        System.out.println("L");


    }
}
