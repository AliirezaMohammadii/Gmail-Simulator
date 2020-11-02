package Controller;

import App.Main;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConversationListItemController {
    Conversation conversation;
    public String errorMessageImageURL = "src\\Resources\\Shapes\\icons8-error-filled-100.png";

    @FXML
    public AnchorPane root;
    @FXML
    public ImageView imageField;
    @FXML
    public Text nameField;
    @FXML
    public Text subjectField;
    @FXML
    public Text someCharsField;
    @FXML
    public Text DateField;
    @FXML
    public Text timeField;
    @FXML
    public Text emailAddress;
    @FXML
    public Text numOfStarredMessages;

    public ConversationListItemController(Conversation conversation) throws IOException {
        this.conversation = conversation;
        load("View/ConversationListItem.fxml", this);
    }

    public AnchorPane init() {
        numOfStarredMessages.setText(String.valueOf(conversation.numOfStarredMessaged));
        if (conversation.IMAGE_URL.equals("")) {
            imageField.setImage(new Image(Paths.get(errorMessageImageURL).toUri().toString()));
        } else {
            imageField.setImage(new Image(Paths.get(conversation.IMAGE_URL).toUri().toString()));
        }
        emailAddress.setText(conversation.messageList.get(0).getSender().getUsername() +
                "@gmail.com");
        if ((Client.currentReceivedMessage.getSubject().equals("UnAvailable Address Error")) &&
                AllFirstMessageReceivedConversations.list.contains(conversation))
            emailAddress.setText("mailerdaemon@googlemail.com");
        DateField.setText(conversation.messageList.get(conversation.messageList.size() - 1).Date);
        timeField.setText(conversation.messageList.get(conversation.messageList.size() - 1).Time);
        for (Message message : conversation.messageList) {
            if (!message.isRead) {
                conversation.haveUnReadMessage = true;
//                System.err.println("now : conversation has unRead mess/age");
                break;
            }
            conversation.haveUnReadMessage = false;
        }
        for (Message message : conversation.messageList) {
            if (message.isFavorite) {
//                System.err.println("now : conversation has starred message");
                conversation.haveStarredMessage = true;
                break;
            }
            conversation.haveStarredMessage = false;
        }
        if (conversation.haveUnReadMessage) {
            System.err.println("now : blue color");
            root.setBackground(new Background(new BackgroundFill(Color.web("#1d5d99"), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            System.err.println("now : gray color");
            root.setBackground(new Background(new BackgroundFill(Color.web("#ccd2d8"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
//        if ((Client.currentReceivedMessage.getSubject().equals("UnAvailable Address Error")) &&
//        AllFirstMessageReceivedConversations.list.contains(conversation)) {
//            imageField.setImage(new Image(Paths.get(errorMessageImageURL).toUri().toString()));
//        }
        if (conversation.messageList.size() != 0) {
//            imageField.setImage(new Image(Paths.get(conversation.messageList.get(0).getSender().getProfileImageURL()).toUri().toString()));
            if (conversation.messageList.get(0).getSender().getUsername().equals(Client.currentUser.getUsername())) {
                nameField.setText("Me");
            } else {
                nameField.setText(conversation.messageList.get(0).getSender().getFirstName() + " " +
                        conversation.messageList.get(0).getSender().getLastName());
            }
            subjectField.setText(conversation.getConversationSubject());
            createSomeCharacter();
        } else {
            nameField.setText("No Message In The Conversation!");
        }
        if ((Client.currentReceivedMessage.getSubject().equals("UnAvailable Address Error")) &&
                AllFirstMessageReceivedConversations.list.contains(conversation)) {
            nameField.setText(conversation.messageList.get(0).getSender().getFirstName());
            nameField.setStyle("-fx-font: 14 Constantia");
            subjectField.setStyle("-fx-font: 14 Ebrima");
        }
//        DateField.setMessageText();
//        timeField.setMessageText();
        return root;
    }

    public void createSomeCharacter() {
        String lastMessageText = conversation.messageList.get(conversation.messageList.size() - 1).getMessageText();
        String tempText = "";
        for (int i = 0; i < lastMessageText.length() && i < 30; i++) {
            tempText += lastMessageText.charAt(i);
        }
        someCharsField.setText(tempText + "...");
    }

    public void deleteConversation(MouseEvent mouseEvent) throws IOException {
        String tmpForServer = "[" + Client.currentUser.getUsername() + "] removeconv [" +
                (AllFirstMessageSentConversations.list.contains(conversation) ?
                        Client.currentConversation.getOtherSide() : Client.currentConversation.getFirstMessageSender())
                + "]\n" + "time: [" + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpForServer);
        AllFirstMessageSentConversations.list.remove(conversation);
        AllFirstMessageReceivedConversations.list.remove(conversation);
    }

    public void entryToConversation(MouseEvent mouseEvent) throws IOException {
        Client.currentConversation = conversation;
        if (AllFirstMessageSentConversations.list.contains(conversation))
            ConversationPageController.lastPage = STATES.SENT_PAGE;
        else
            ConversationPageController.lastPage = STATES.INBOX_PAGE;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/ConversationPage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }
    public static String thisTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void load(String url, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(url));
        fxmlLoader.setController(controller);
        fxmlLoader.load();
    }
}
