package GUI;

import Client.Main;
import Hashing.Hashing;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LogInController {

    /** field where user enters the username **/
    @FXML
    private TextField usernameField;

    /** field where user enters a password **/
    @FXML
    private PasswordField passwordField;

    /** button that lets user log in **/
    @FXML
    private Button logInButton;

    /** username hbox **/
    @FXML
    private HBox usernameBox;

    /** password hbox **/
    @FXML
    private HBox passwordBox;

    /** username label **/
    @FXML
    private Label usernameLabel;

    /** password label **/
    @FXML
    private Label passwordLabel;

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
    public void logInMouseEntered(){
        logInButton.setStyle("-fx-background-color: #5040db");
    }

    /** changes border color when mouse stops hovering the button **/
    public void logInMouseExited(){
        logInButton.setStyle("-fx-background-color: #5B49F5");
    }


    public void logInHandler(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        /* if username or password field are empty, return and indicate the user to fill them out */
        if(username.isEmpty() || password.isEmpty()){
            if(username.isEmpty()) {
                addWarning("      please enter a username!", usernameBox);
                makeFieldRed(usernameField, usernameLabel);
            }

            if(password.isEmpty()){
                addWarning("      please enter a password!", passwordBox);
                makePasswordFieldRed(passwordField, passwordLabel);
            }
            return;
        }

        /* check if account if log in successful */
        if(Main.client.logIn(username, password)){
            closeStage();
            System.out.println("Logged in");
            return;
        }

        /* log in not successful */
        addWarning("  username or password are incorrect", usernameBox);
        addWarning("  username or password are incorrect", passwordBox);
        makeFieldRed(usernameField, usernameLabel);
        makePasswordFieldRed(passwordField, passwordLabel);

    }

    /** Sends user to the create account page and closes current page **/
    public void registerHereHandler(){
        closeStage();
        GUI.createAccountPage();
    }

    /** closes current stage **/
    private void closeStage(){
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void addWarning(String message, HBox box){
        Label warning = new Label(message);
        warning.setId("warning");
        if(box.getChildren().size() > 1) box.getChildren().remove(1);

        box.getChildren().add(warning);
    }

    private void makeFieldRed(TextField field, Label label){
        label.setStyle("-fx-text-fill: #e81515");
        field.setStyle("-fx-border-color: #e81515");
    }

    private void makePasswordFieldRed(PasswordField field, Label label){
        label.setStyle("-fx-text-fill: #e81515");
        field.setStyle("-fx-border-color: #e81515");
    }

}
