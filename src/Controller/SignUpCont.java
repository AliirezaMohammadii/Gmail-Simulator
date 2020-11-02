package Controller;

import App.Main;
import Model.Client;
import Model.STATES;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class SignUpCont {

    public TextField firstNameField;
    public TextField lastNameField;
    public TextField passwordField;
    public TextField usernameField;
    public TextField birthYearField;
    public TextField birthMonthField;
    public TextField birthDayField;

    public void signUp() throws IOException, ClassNotFoundException {

        if (checkEmptyFields()) return;

        if (checkUsernameValidity()) return;

        if (checkPasswordValidity()) return;

        if (checkAgeValidity()) return;

        makeUser();

        Client.state = STATES.SIGN_UP;
        Client.send.checkSignUpRequest();
    }

    public static void showRestOfInfPage() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SignUpCont.class.getClassLoader().getResource("View/restOfInformation.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    private void makeUser() {
        Client.currentUser = new User(usernameField.getText(), passwordField.getText(),
                firstNameField.getText(), lastNameField.getText(), birthYearField.getText(),
                birthMonthField.getText(), birthDayField.getText());
//        Client.received_user_information = true;
    }

    private boolean checkEmptyFields() {
        if (usernameField.getText().equals("") || passwordField.getText().equals("") ||
                firstNameField.getText().equals("") || lastNameField.getText().equals("") ||
                birthDayField.getText().equals("") || birthMonthField.getText().equals("") ||
                birthYearField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PLEASE COMPLETE FIELDS!");
            alert.show();
            return true;
        }
        return false;
    }

    public static void showRepetitiveUsernameAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "USERNAME IS REPETITIVE!\n" +
                "PLEASE ENTER SOMETHING ELSE");
        alert.show();
    }

//    public static boolean checkRepetitiveUsername() {
//        if(Client.repetitive_Username){
//            Alert alert = new Alert(Alert.AlertType.ERROR, "USERNAME IS REPETITIVE!\n" +
//                    "PLEASE ENTER SOMETHING ELSE");
//            alert.show();
//            Client.repetitive_Username = false;
//            return true;
//        }
//        return false;
//    }

    private boolean checkAgeValidity() {

        try {
            Integer year = Integer.parseInt(birthYearField.getText());
            Integer month = Integer.parseInt(birthMonthField.getText());
            Integer day = Integer.parseInt(birthDayField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID NUMBERS IN FIELDS RELATE TO USER'S AGE !");
            alert.show();
            return true;
        }

        if (Integer.valueOf(birthYearField.getText()) > 2006) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "YOU ARE SO YOUNG TO CREATE ACCOUNT!");
            alert.show();
            return true;
        }

        if (Integer.valueOf(birthYearField.getText()) > 2019 || Integer.valueOf(birthMonthField.getText()) > 12 ||
                Integer.valueOf(birthDayField.getText()) > 31) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID DATE FOR AGE !");
            alert.show();
            return true;
        }

        return false;
    }

    private boolean checkPasswordValidity() {
        if (passwordField.getText().length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PASSWORD MUST CONTAINS 8 CHARACTERS AT LEAST!");
            alert.show();
            return true;
        }
        boolean capitalLetter = false;
        boolean smallLetter = false;
        boolean numericLetter = false;
        for (int i = 0; i < passwordField.getText().length(); i++) {
            char c = passwordField.getText().charAt(i);
            if (!capitalLetter) {
                if (c >= 65 && c <= 90)
                    capitalLetter = true;
            }
            if (!smallLetter) {
                if (c >= 97 && c <= 122)
                    smallLetter = true;
            }
            if (!numericLetter) {
                if (c >= 48 && c <= 57)
                    numericLetter = true;
            }
        }
        if (!(numericLetter && capitalLetter && smallLetter)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "PASSWORD MUST CONTAINS A-Z & a-z & 0-9" +
                    "CHARACTERS!");
            alert.show();
            return true;
        }
        return false;
    }

    private boolean checkUsernameValidity() {
        for (int i = 0; i < usernameField.getText().length(); i++) {
            char c = usernameField.getText().charAt(i);
            if (!((c >= 65 && c <= 90) || (c >= 48 && c <= 57) || (c >= 97 && c <= 122) || c == 46)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID USERNAME!");
                alert.show();
                return true;
            }
        }
        return false;
    }

    public void back(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/SignIn.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }
}
