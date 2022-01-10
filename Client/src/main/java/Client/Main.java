package Client;

import GUI.FeedPage;
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
        new FeedPage(null);
        //client.getPosts();

        //ArrayList<Post> posts = client.getPosts();
        //client.downloadImages(posts);

        /*
        String path = ImageExplorer.getPath();
        Post post = new Post("It feel like forever", "spooderman", ImageExplorer.convertImageToByteArray(path, ImageExplorer.getImageType(path)));
        client.publishPost(post, path);

        path = ImageExplorer.getPath();
        post = new Post("Seems fun", "spooderman", ImageExplorer.convertImageToByteArray(path, ImageExplorer.getImageType(path)));
        client.publishPost(post, path);

        path = ImageExplorer.getPath();
        post = new Post("pineapple on pizza??", "spooderman", ImageExplorer.convertImageToByteArray(path, ImageExplorer.getImageType(path)));
        client.publishPost(post, path);
        */
    }
}
