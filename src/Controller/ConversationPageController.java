package Controller;

import App.Main;
import Model.Client;
import Model.Message;
import Model.STATES;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConversationPageController {
    public static STATES lastPage;

    public ListView<Message> messagesList;
    public Text conversationSubject;
    public ImageView backImage;
    public ImageView blockUserImage;
    public ImageView updateImage;

    public void initialize() {
        conversationSubject.setText(Client.currentConversation.getConversationSubject());
        if (Client.currentConversation.getConversationSubject().equals("UnAvailable Address Error")) {
            conversationSubject.setStyle("-fx-font: 15 System");
        }
        for (Message message : Client.currentConversation.messageList) {
            message.isRead = true;
        }
        Client.currentConversation.haveUnReadMessage = false;
        messagesList.setItems(FXCollections.observableArrayList(Client.currentConversation.messageList));
        messagesList.setCellFactory(userListView -> new MessageListItem());
    }

    public void select(MouseEvent mouseEvent) {
    }

//    public static void updateItems(){
//        mess.setItems(FXCollections.observableArrayList(Client.currentConversation.messageList));
//        mess.setCellFactory(userListView -> new MessageListItem());
//    }

    public void back(MouseEvent mouseEvent) throws IOException {
        if (lastPage == STATES.INBOX_PAGE) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/inbox.fxml")));
            Main.ps.setScene(new Scene(root));
            return;
        } else {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/sent.fxml")));
            Main.ps.setScene(new Scene(root));
            return;
        }
    }

    public static String thisTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void blockUser(MouseEvent mouseEvent) throws IOException {
        if (lastPage == STATES.SENT_PAGE) {
            String tmpForServer = "[" + Client.currentUser.getUsername() + "] [block] [" +
                    Client.currentConversation.getOtherSide() + "]\n" + "time: [" + thisTime() + "]";
            Client.send.sendTempMessageForServer(tmpForServer);
            Client.currentUser.blockedUsersList.add(Client.currentConversation.getOtherSide());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "USER BLOCKED\n" +
                    "AND THE CONVERSATION DELETED!");
            alert.show();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/sent.fxml")));
            Main.ps.setScene(new Scene(root));
            return;
        } else {
            String tmpForServer = "[" + Client.currentUser.getUsername() + "] [block] [" +
                    Client.currentConversation.getFirstMessageSender() + "]\n" + "time: [" + thisTime() + "]";
            Client.send.sendTempMessageForServer(tmpForServer);
            Client.currentUser.blockedUsersList.add(Client.currentConversation.getFirstMessageSender());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "USER BLOCKED\n" +
                    "AND THE CONVERSATION DELETED!");
            alert.show();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/inbox.fxml")));
            Main.ps.setScene(new Scene(root));
            return;
        }
    }

    public void updateList(MouseEvent mouseEvent) {
        messagesList.setItems(FXCollections.observableArrayList(Client.currentConversation.messageList));
        messagesList.setCellFactory(userListView -> new MessageListItem());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "CONVERSATION UPDATED!");
        alert.show();
        return;
    }
}
