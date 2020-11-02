package Controller;

import App.Main;
import Model.Client;
import Model.ImagesList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class restOfInformationController {
    public String selected;
    public TextField phoneNumberField;
    public ImageView imageField;
    public ListView<String> imagesList;
    public Text sexualityField;
    public Button button;

    public void initialize(){
        selected = "src/Resources/images/unKnown.png";
        imagesList.setItems(FXCollections.observableArrayList(ImagesList.imagesList));
        imagesList.setCellFactory(userListView -> new ImageListItem());
        Client.currentUser.setProfileImageURL("src/Resources/images/unKnown.png");
//        button.setOnAction(event -> {
//            FileChooser chooser = new FileChooser();
//            File file = chooser.showOpenDialog(Main.ps);
//        });
    }


    public void select(MouseEvent mouseEvent) {
        selected = imagesList.getSelectionModel().getSelectedItem();
        imageField.setImage(new Image(Paths.get(selected).toUri().toString()));
    }

    public void submitInformation(ActionEvent actionEvent) throws IOException {

        if(phoneNumberField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR, "PLEASE COMPLETE FIELDS!");
            alert.show();
            return;
        }

        try {
            Double phoneNumber = Double.parseDouble(phoneNumberField.getText());
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "INVALID PHONE NUMBER!");
            alert.show();
            return;
        }

        Client.currentUser.setPhoneNumber(phoneNumberField.getText());
        Client.currentUser.setSexuality(sexualityField.getText());
        Client.currentUser.setProfileImageURL(selected);
        Client.send.sendInfToServer();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/mainPage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void male(MouseEvent mouseEvent) {
        sexualityField.setText("Male");
    }

    public void female(MouseEvent mouseEvent) {
        sexualityField.setText("Female");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/SignUp.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void defaultPicture(MouseEvent mouseEvent) {
        imageField.setImage(new Image(Paths.get("src/Resources/images/unKnown.png").toUri().toString()));
    }
}
