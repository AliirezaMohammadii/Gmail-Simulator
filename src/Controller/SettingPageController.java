package Controller;

import App.Main;
import Model.Client;
import Model.ImagesList;
import Model.STATES;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class SettingPageController {
    public Text firstNameField;
    public Text lastNameField;
    public Text passwordFiled;
    public Text birthDateFiled;
    public Text PhoneNumberField;
    public Text SexualityField;
    public ImageView profileImage;
    public TextField changeFirstNameField;
    public ImageView doneImage;
    public TextField changeLastNameField;
    public TextField changePasswordField;
    public TextField changePhoneNumberField;
    public static STATES changeState;
    public ListView<String> imagesList;
    public String selected;
    public HBox birthDateHBox;
    public TextField yearField;
    public TextField monthField;
    public TextField dayField;

    public void initialize() {
        profileImage.setImage(new Image(Paths.get(Client.currentUser.getProfileImageURL()).toUri().toString()));
        firstNameField.setText(Client.currentUser.getFirstName());
        lastNameField.setText(Client.currentUser.getLastName());
        passwordFiled.setText(Client.currentUser.getPassword());
        PhoneNumberField.setText(Client.currentUser.getPhoneNumber());
        SexualityField.setText(Client.currentUser.getSexuality());
        birthDateFiled.setText(Client.currentUser.getBirthYear() + " / " + Client.currentUser.getBirthMonth()
                + " / " + Client.currentUser.getBirthDay());
        imagesList.setItems(FXCollections.observableArrayList(ImagesList.imagesList));
        imagesList.setCellFactory(userListView -> new ImageListItem());
    }

    public void firstNameChange(MouseEvent mouseEvent) {
        setTextsVisible();
        setTextFieldsUnVisible();
        changeState = STATES.CHANGE_FIRST_NAME;
        changeFirstNameField.setVisible(true);
        firstNameField.setVisible(false);
        doneImage.setVisible(true);
    }

    public void lastNameChange(MouseEvent mouseEvent) {
        setTextsVisible();
        setTextFieldsUnVisible();
        changeState = STATES.CHANGE_LAST_NAME;
        changeLastNameField.setVisible(true);
        lastNameField.setVisible(false);
        doneImage.setVisible(true);
    }

    public void passwordChange(MouseEvent mouseEvent) {
        setTextsVisible();
        setTextFieldsUnVisible();
        changeState = STATES.CHANGE_PASSWORD;
        changePasswordField.setVisible(true);
        passwordFiled.setVisible(false);
        doneImage.setVisible(true);
    }

    public void birthDateChange(MouseEvent mouseEvent) {
        setTextsVisible();
        setTextFieldsUnVisible();
        changeState = STATES.CHANGE_BORN;
        birthDateHBox.setVisible(true);
        birthDateFiled.setVisible(false);
        doneImage.setVisible(true);
    }

    public void phoneNumberChange(MouseEvent mouseEvent) {
        setTextsVisible();
        setTextFieldsUnVisible();
        changeState = STATES.CHANGE_PHONENUMBER;
        changePhoneNumberField.setVisible(true);
        PhoneNumberField.setVisible(false);
        doneImage.setVisible(true);
    }

    public void changeProfileImage(MouseEvent mouseEvent) {
        setTextsVisible();
        selected = Client.currentUser.getProfileImageURL();
        setTextFieldsUnVisible();
        doneImage.setVisible(true);
        imagesList.setVisible(true);
        changeState = STATES.CHANGE_PROFILE_IMAGE;

    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/mainPage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void done(MouseEvent mouseEvent) {
        switch (changeState) {
            case CHANGE_FIRST_NAME: {
                if (!changeFirstNameField.getText().equals(""))
                    firstNameField.setText(changeFirstNameField.getText());
                firstNameField.setVisible(true);
                changeFirstNameField.setVisible(false);
                Client.currentUser.setFirstName(firstNameField.getText());
            }
            break;
            case CHANGE_LAST_NAME: {
                if (!changeLastNameField.getText().equals(""))
                    lastNameField.setText(changeLastNameField.getText());
                lastNameField.setVisible(true);
                changeLastNameField.setVisible(false);
                Client.currentUser.setLastName(lastNameField.getText());
            }
            break;
            case CHANGE_PASSWORD: {
                if (!changePasswordField.getText().equals(""))
                    passwordFiled.setText(changePasswordField.getText());
                passwordFiled.setVisible(true);
                changePasswordField.setVisible(false);
                Client.currentUser.setPassword(passwordFiled.getText());
            }
            break;
            case CHANGE_BORN: {
                if (!(yearField.getText().equals("") && monthField.getText().equals("") || dayField.getText().equals(""))) {
                    try {
                        Integer year = Integer.parseInt(yearField.getText());
                        Integer month = Integer.parseInt(monthField.getText());
                        Integer day = Integer.parseInt(dayField.getText());
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID DATE FOR AGE !");
                        alert.show();
                        break;
                    }
                    birthDateFiled.setText(yearField.getText() + " / " + monthField.getText() + " / " + dayField.getText());
                    Client.currentUser.setBirthYear(yearField.getText());
                    Client.currentUser.setBirthMonth(monthField.getText());
                    Client.currentUser.setBirthDay(dayField.getText());
                }
                birthDateFiled.setVisible(true);
                birthDateHBox.setVisible(false);
            }
            break;
            case CHANGE_PHONENUMBER: {
                if (!changePhoneNumberField.getText().equals(""))
                    PhoneNumberField.setText(changePhoneNumberField.getText());
                PhoneNumberField.setVisible(true);
                changePhoneNumberField.setVisible(false);
                Client.currentUser.setPhoneNumber(PhoneNumberField.getText());
            }
            break;
            case CHANGE_PROFILE_IMAGE: {
                imagesList.setVisible(false);
                Client.currentUser.setProfileImageURL(selected);
            }
        }
        deleteFieldsText();
        doneImage.setVisible(false);
    }

    public void setTextFieldsUnVisible() {
        birthDateHBox.setVisible(false);
        changePhoneNumberField.setVisible(false);
        changeFirstNameField.setVisible(false);
        changeLastNameField.setVisible(false);
        changePasswordField.setVisible(false);
        imagesList.setVisible(false);
    }

    public void setTextsVisible() {
        firstNameField.setVisible(true);
        lastNameField.setVisible(true);
        passwordFiled.setVisible(true);
        PhoneNumberField.setVisible(true);
        birthDateFiled.setVisible(true);
    }

    public void deleteFieldsText() {
        dayField.setText("");
        monthField.setText("");
        yearField.setText("");
        changePhoneNumberField.setText("");
        changeFirstNameField.setText("");
        changeLastNameField.setText("");
        changePasswordField.setText("");
    }

    public void selectImage(MouseEvent mouseEvent) {
        selected = imagesList.getSelectionModel().getSelectedItem();
        profileImage.setImage(new Image(Paths.get(selected).toUri().toString()));
    }

    public void changeSexuality(MouseEvent mouseEvent) {
        if (SexualityField.getText().equals("Male"))
            SexualityField.setText("Female");
        else {
            SexualityField.setText("Male");
        }
    }
}
