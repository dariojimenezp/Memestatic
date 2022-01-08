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
        createStage("Start", "Start");
    }

    public static void createAccountPage(){
        createStage("CreateAccount", "CreateAccount");
    }

    private static void createStage(String fxmlFile, String CSSFile){
        /* load fxml file */
        Parent root = null;
        try {
            root = FXMLLoader.load(GUI.class.getResource("../fxml/" + fxmlFile + ".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* set up scene and add CSSs style sheet to it */
        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(GUI.class.getResource("../Styles/" + CSSFile + ".css").toExternalForm());

        /* set up the stage */
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
