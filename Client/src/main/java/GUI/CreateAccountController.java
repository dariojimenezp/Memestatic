package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateAccountController {

    /** field where user enters the username **/
    @FXML
    private TextField usernameField;

    /** field where user enters a password **/
    @FXML
    private PasswordField passwordField;

    /** create account button **/
    @FXML
    private Button createAccountButton;

    /** username handlers **/

    /** changes the color of the border when user clicks on field and resets the other field's color **/
    public void usernameFieldClicked(){
        usernameField.setStyle("-fx-border-color: #5B49F5");
        passwordField.setStyle("-fx-border-color: #242323");
    }

    /** changes border color when mouse enters **/
    public void usernameMouseEntered(){
        usernameField.setStyle("-fx-border-color: #242323");
    }

    /** changes border color when mouse enters **/
    public void usernameMouseExited(){
        usernameField.setStyle("-fx-border-color: #2b2b2b");
    }

    /** password handlers **/

    /** changes the color of the border when user clicks on field **/
    public void passwordFieldClicked(){
        passwordField.setStyle("-fx-border-color: #5B49F5");
        usernameField.setStyle("-fx-border-color: #242323");
    }

    /** changes border color when mouse hovers the field **/
    public void passwordMouseEntered(){
        passwordField.setStyle("-fx-border-color: #242323");
    }

    /** changes border color when mouse stops hovering the field **/
    public void passwordMouseExited(){
        passwordField.setStyle("-fx-border-color: #2b2b2b");
    }


    /** create account button handlers **/

    /** changes border color when mouse hovers the button **/
    public void createAccountMouseEntered(){
        createAccountButton.setStyle("-fx-background-color: #5040db");
    }

    /** changes border color when mouse stops hovering the button **/
    public void createAccountMouseExited(){
        createAccountButton.setStyle("-fx-background-color: #5B49F5");
    }

    public void createAccountHandler(){
    }

    /** takes user to log in page and closes current page **/
    public void logInHereHandler(){
        closeStage();
        GUI.logInPage();
    }

    /** closes current stage **/
    public void closeStage(){
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
