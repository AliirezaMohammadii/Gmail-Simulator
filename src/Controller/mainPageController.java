package Controller;

import App.Main;
import Model.AllFirstMessageReceivedConversations;
import Model.AllFirstMessageSentConversations;
import Model.Client;
import Model.STATES;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class mainPageController {
    public ImageView profileImageFiled;
    public Text nameField;
    public Text EmailAddressField;
    public ImageView sentListImage;
    public ImageView outboxImage;
    public ImageView inboxListImage;
    public ImageView createMessageImage;
    public ImageView backImage;

    public void initialize(){
        profileImageFiled.setImage(new Image(Paths.get(Client.currentUser.getProfileImageURL()).toUri().toString()));
//        profileImageFiled.setClip(new Circle(60 , 60 , 60));
        nameField.setText(Client.currentUser.getFirstName() + " " + Client.currentUser.getLastName());
        EmailAddressField.setText(Client.currentUser.getUsername() + "@gmail.com");
    }

    public void outboxPage(MouseEvent mouseEvent) throws IOException {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/outbox.fxml")));
//        Main.ps.setScene(new Scene(root));
//        return;
    }

    public void inboxPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/inbox.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void sentMessagesPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/sent.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void createNewMessage(MouseEvent mouseEvent) throws IOException {
        sendMessageController.repFor = STATES.NORMAL_MESSAGE;
        sendMessageController.lastPage = STATES.MAIN_PAGE;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/sendMessage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void back(MouseEvent mouseEvent) throws IOException {

        Client.out.writeObject(STATES.FINALIZE.toString());
        Client.out.flush();
        Client.out.writeObject(Client.currentUser.getUsername());
        Client.out.flush();
        Client.out.writeObject(AllFirstMessageSentConversations.list);
        Client.out.flush();
        Client.out.writeObject(AllFirstMessageReceivedConversations.list);
        Client.out.flush();
        System.err.println("lists sent to user in stop method!");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/SignIn.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void settingPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/Setting.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }
}
