package Controller;

import App.Main;
import Model.Client;
import Model.Message;
import Model.STATES;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MessageListItemController {

    Message message;
    @FXML
    AnchorPane root;
    @FXML
    Text senderName;
    @FXML
    Text senderEmailAddress;
    @FXML
    Text Subject;
    @FXML
    Text date;
    @FXML
    Text time;
    @FXML
    ImageView senderImage;
    @FXML
    ImageView noFileImage;
    @FXML
    ImageView fileImage;
    @FXML
    Text fileAddressText;
    @FXML
    TextField fileAddressTextField;
    @FXML
    Text noFileField;
    @FXML
    Text messageText;
    @FXML
    ImageView emptyStarImage;
    @FXML
    ImageView fillStarImage;
    @FXML
    ImageView emptyTickImage;
    @FXML
    ImageView fillTickImage;
    @FXML
    ImageView replyImage;
    @FXML
    ImageView forwardImage;
    @FXML
    ImageView deleteImage;
    @FXML
    ImageView zarbdarImage;
    public String errorMessageImageURL = "src\\Resources\\Shapes\\icons8-error-filled-100.png";

    public MessageListItemController(Message message) throws IOException {
        this.message = message;
        load("View/messageListItem.fxml", this);
    }

    public AnchorPane init() {
        if (!message.getSender().equals(Client.currentUser)) {
//            System.err.println("1111111111111111111");
//            System.err.println("FILE ADDRESS : " + Client.receivedFileAddress);
            message.setFileAddress(Client.receivedFileAddress);
        }
        if (!message.getFileAddress().equals("")) {
//            System.err.println("2222222222222222222");
            noFileImage.setVisible(false);
            fileImage.setVisible(true);
            fileAddressTextField.setText(message.getFileAddress());
//            System.out.println("chizi ke bayad tu fileAddressTextField neveshte she!");
            if (message.getFileAddress().contains("mp3"))
                noFileField.setText("Music");
            else if (message.getFileAddress().contains("jpg"))
                noFileField.setText("Picture");
            else {
                noFileField.setText("Video");
            }
        }
        if (message.isFavorite) {
            fillStarImage.setVisible(true);
            emptyStarImage.setVisible(false);
        } else {
//            System.err.println("message is not fave!");
            fillStarImage.setVisible(false);
            emptyStarImage.setVisible(true);
        }
        if (message.isRead) {
            fillTickImage.setVisible(true);
            emptyTickImage.setVisible(false);
        }
        senderName.setText(message.getSender().getFirstName() + " " + message.getSender().getLastName());
        if (message.getSender().equals(Client.currentUser)) {
            senderName.setText("Me");
        }
        senderEmailAddress.setText(message.getSender().getUsername() + "@gmail.com");
        Subject.setText(message.getSubject());
        date.setText(message.Date);
        time.setText(message.Time);
        if (message.getSender().getProfileImageURL().equals("")) {
            senderImage.setImage(new Image(Paths.get(errorMessageImageURL).toUri().toString()));
            senderEmailAddress.setText(message.getSender().getUsername());
            senderEmailAddress.setStyle("-fx-font: 14 System");
            senderName.setText(message.getSender().getFirstName());
        } else {
            senderImage.setImage(new Image(Paths.get(message.getSender().getProfileImageURL()).toUri().toString()));
        }
//        senderImage.setClip(new Circle(40, 40, 40));
//        fileImage.setImage();
        messageText.setText(message.getMessageText());
        return root;
    }

    public void showFile(MouseEvent mouseEvent) {
        fileAddressTextField.setVisible(true);
        fileAddressText.setVisible(true);
        zarbdarImage.setVisible(true);
    }

    public static String thisTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void emptyStarAction(MouseEvent mouseEvent) throws IOException {

        String tmpForServer = "[" + Client.currentUser.getUsername() + "] mark\n" +
                "message: [" + Subject.getText() + "] [" + senderName.getText() + "] as [" +
                "important" + "]\n" + "time: " + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpForServer);

        emptyStarImage.setVisible(false);
        fillStarImage.setVisible(true);
        message.getRelatedConversation().numOfStarredMessaged++;
        message.getRelatedConversation().haveStarredMessage = true;
        message.isFavorite = true;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "FAVED!");
        alert.show();
        return;
    }

    public void fillStarAction(MouseEvent mouseEvent) throws IOException {
        String tmpForServer = "[" + Client.currentUser.getUsername() + "] mark\n" +
                "message: [" + Subject.getText() + "] [" + senderName.getText() + "] as [" +
                "unimportant" + "]\n" + "time: " + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpForServer);

        emptyStarImage.setVisible(true);
        fillStarImage.setVisible(false);
        message.getRelatedConversation().numOfStarredMessaged--;
        message.isFavorite = false;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "UN FAVED!");
        alert.show();
        return;
    }

    public void emptyTickAction(MouseEvent mouseEvent) throws IOException {
        String tmpForServer = "[" + Client.currentUser.getUsername() + "] mark\n" +
                "message: [" + Subject.getText() + "] [" + senderName.getText() + "] as [" +
                "read" + "]\n" + "time: " + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpForServer);
        emptyTickImage.setVisible(false);
        fillTickImage.setVisible(true);
        message.isRead = true;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "COLORNESS TICK!");
        alert.show();
        return;
    }

    public void fillTickAction(MouseEvent mouseEvent) throws IOException {
        String tmpForServer = "[" + Client.currentUser.getUsername() + "] mark\n" +
                "message: [" + Subject.getText() + "] [" + senderName.getText() + "] as [" +
                "unread" + "]\n" + "time: [" + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpForServer);

        message.getRelatedConversation().haveUnReadMessage = true;
        emptyTickImage.setVisible(true);
        fillTickImage.setVisible(false);
        message.isRead = false;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "MESSAGE STATE CHANGED TO UNREAD!");
        alert.show();
        return;
    }

    public void replyMessage(MouseEvent mouseEvent) throws IOException {
        Client.currentSendingMessage.setSender(Client.currentUser);
        Client.currentSendingMessage.setRelatedConversation(message.getRelatedConversation());
//        System.err.println("Check Subjects equality in first step :\n" +
//                "subject of message i selected reply on it : " + message.getSubject() +
//                "\n subject of client currentSendingMessage's relatedConversation's subject : " +
//                Client.currentSendingMessage.getRelatedConversation().getConversationSubject() +
//                "\n message i selected reply on it 's relatedConversation's subject : " +
//                message.getRelatedConversation().getConversationSubject());
        Client.currentSendingMessage.setReceiver(message.getReceiver());
        Client.currentSendingMessage.setSubject(message.getSubject());
//        sendMessageController.receiverField.setMessageText(message.getReceiver());
        sendMessageController.lastPage = STATES.CONVERSATION_PAGE;
        sendMessageController.repFor = STATES.REPLY;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/sendMessage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void forwardMessage(MouseEvent mouseEvent) throws IOException {
        sendMessageController.lastPage = STATES.CONVERSATION_PAGE;
        sendMessageController.repFor = STATES.FORWARD;
        Client.currentSendingMessage.setFileAddress(message.getFileAddress());
//        sendMessageController.fileAddress = (message.getFileAddress());
        Client.currentSendingMessage.setMessageText(message.getMessageText());
//        System.err.println("message text that must be put in forwarding message textArea ->>>   " + message.getMessageText());
//        sendMessageController.textAreaField.setMessageText(messageText.getMessageText());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/sendMessage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void deleteMessage(MouseEvent mouseEvent) throws IOException {
        String tmpForServer = "[" + Client.currentUser.getUsername() + "] removemsg\n" +
                "message: [" + Subject.getText() + "] [" + senderName.getText() + "]\n" + "time: [" + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpForServer);
        message.getRelatedConversation().messageList.remove(message);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "MESSAGE DELETED!" +
                "UPDATE THE LIST PLEASE!");
        alert.show();
        return;
    }

    public void removeFileAddressFields(MouseEvent mouseEvent) {
        fileAddressTextField.setVisible(false);
        fileAddressText.setVisible(false);
        zarbdarImage.setVisible(false);
    }


    public void load(String url, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(url));
        fxmlLoader.setController(controller);
        fxmlLoader.load();
    }
}
