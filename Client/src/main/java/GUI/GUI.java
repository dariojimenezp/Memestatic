package GUI;

import Client.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI {


    public static void start(){
         createStage("Start");
    }

    public static void createAccountPage(){
        createStage("CreateAccount");
    }

    public static void logInPage(){
        createStage("LogIn");
    }

    private static Stage createStage(String page){
        /* load fxml file */
        Parent root = null;
        try {
            root = FXMLLoader.load(GUI.class.getResource("/fxml/" + page + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* set up scene and add CSSs style sheet to it */
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(GUI.class.getResource("/Styles/" + page + ".css").toExternalForm());

        /* set up the stage */
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();


        return stage;
    }


}
