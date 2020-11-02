package Controller;

import App.Main;
import Model.STATES;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Model.Client;

import java.io.IOException;
import java.util.Objects;

public class SignInCont {

    public static boolean userSignedInSuccessfully = false;

    public TextField usernameField;
    public TextField passwordField;
    public Button signIn;

    public void initialize() throws IOException {
        Client.start();
        Client.state = STATES.NO_REQUEST;
//        signIn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                signIn.setVisible(false);
//            }
//        });
    }

    public void signIn(ActionEvent actionEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if (usernameField.getText().equals("") || passwordField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PLEASE COMPLETE FIELDS!");
            alert.show();
            return;
        }

        Client.SignIn_Username = usernameField.getText();
        Client.SignIn_Password = passwordField.getText();

        Client.state = STATES.SIGN_IN;
        Client.send.checkSignInRequest();
//        if(userSignedInSuccessfully){
//            Client.state = STATES.NO_REQUEST;
//            showMainPanelPage();
//        }
    }

    public static void wrongPasswordAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR, "WRONG PASSWORD!");
        alert.show();
        return;
    }

    public static void notFoundUsername(){
        Alert alert = new Alert(Alert.AlertType.ERROR, "USER NOT FOUND!");
        alert.show();
        return;
    }

    public void signUp(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/SignUp.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public static void showMainPanelPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SignUpCont.class.getClassLoader().getResource("View/mainPage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }
}
