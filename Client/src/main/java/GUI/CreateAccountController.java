package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateAccountController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button createAccountButton;

    public void usernameChangeBorderColor(){
        usernameField.setStyle("-fx-border-color: #5B49F5");
        passwordField.setStyle("-fx-border-color: #242323");
    }

    public void passwordChangeBorderColor(){
        passwordField.setStyle("-fx-border-color: #5B49F5");
        usernameField.setStyle("-fx-border-color: #242323");
    }

    public void passwordMouseEntered(){
        passwordField.setStyle("-fx-border-color: #242323");
    }

    public void passwordMouseExited(){
        passwordField.setStyle("-fx-border-color: #2b2b2b");
    }

    public void usernameMouseEntered(){
        usernameField.setStyle("-fx-border-color: #242323");
    }

    public void usernameMouseExited(){
        usernameField.setStyle("-fx-border-color: #2b2b2b");
    }

    /** create account button handlers **/

    public void createAccountMouseEntered(){
        createAccountButton.setStyle("-fx-background-color: #5040db");
    }

    public void createAccountMouseExited(){
        createAccountButton.setStyle("-fx-background-color: #5B49F5");
    }

    public void createAccountHandler(){
    }


}
