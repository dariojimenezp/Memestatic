package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class StartController {

    @FXML
    private ImageView logo;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button logInButton;

    @FXML
    private Button startButton;

    /** Handlers for start button **/

    /**
     * Handler for start button
     */
    public void StartButtonHandler(){
        createAccountButton.setText("No");
        System.out.println("l");
    }

    /** Handlers for log in button **/

    /**
     * Handler for log in button
     */
    public void logInHandler(){

    }


    /** Handlers for create account button **/

    /**
     * Handler for create account button
     */
    public void createAccountHandler(){
        Stage stage = (Stage) createAccountButton.getScene().getWindow();
        stage.close();
        GUI.createAccountPage();
    }
}
