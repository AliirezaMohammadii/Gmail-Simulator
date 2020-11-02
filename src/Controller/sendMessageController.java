package Controller;

import App.Main;
import Model.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class sendMessageController {
    //    public static String fileAddress = "";
    public ImageView backImage;
    public ImageView sendMessageImage;
    public ImageView selectFileImage;
    public static STATES lastPage;
    public TextField receiverAddressFiled;
    public TextField subjectFiled;
    public TextArea messageText;
    public static STATES repFor;
    public VBox fileTypeVBox;
    public ListView<String> filesListView;
    public ImageView fileSelectedImage;
    public Text fileTypeText;
    public Text fileAddressText;
    public StackPane selectFileStackPane;
    public String selected;
    public ImageView doneImage;
    public ImageView cancelImage;

    /**
     * initialization this page
     */
    public void initialize() {
        fileTypeText.setText("");
        fileAddressText.setText("");
        if (lastPage == STATES.CONVERSATION_PAGE) {
            if (repFor == STATES.REPLY) {
                if (Client.currentConversation.getFirstMessageSender().equals(Client.currentUser.getUsername()))
                    receiverAddressFiled.setText(Client.currentConversation.getOtherSide());
                else
                    receiverAddressFiled.setText(Client.currentConversation.getFirstMessageSender());
                subjectFiled.setText(Client.currentSendingMessage.getSubject());
            } else {
                fileAddressText.setText(Client.currentSendingMessage.getFileAddress());
                if (fileAddressText.getText().contains("mp3"))
                    fileTypeText.setText("Music");
                else if (fileAddressText.getText().contains("jpg"))
                    fileTypeText.setText("Picture");
                else
                    fileTypeText.setText("Video");
                messageText.setText(Client.currentSendingMessage.getMessageText());
            }
        }
    }


    public void back(MouseEvent mouseEvent) throws IOException {
        if (lastPage == STATES.MAIN_PAGE) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/mainPage.fxml")));
            Main.ps.setScene(new Scene(root));
            return;
        } else {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/ConversationPage.fxml")));
            Main.ps.setScene(new Scene(root));
            return;
        }
    }

    /**
     * this method sets time
     * @return
     */
    public static String thisTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * this method sends message to server to handle
     * @param mouseEvent
     * @throws IOException
     */
    public void sendMessage(MouseEvent mouseEvent) throws IOException {
        if (lastPage == STATES.MAIN_PAGE)
            Client.currentSendingMessage = new Message();
        setTime();
        Client.currentSendingMessage.setSender(Client.currentUser);
        Client.currentSendingMessage.setReceiver(receiverAddressFiled.getText());
        Client.currentSendingMessage.setSubject(subjectFiled.getText());
//        Client.currentSendingMessage.setFileAddress(fileAddress);
        Client.currentSendingMessage.setMessageText(messageText.getText());
        Client.currentSendingMessage.setFileAddress(fileAddressText.getText());
        Client.state = STATES.SEND_MESSAGE;
        System.out.println("message is ready to sent !");
        sendTempMessageToServer();
        Client.send.sendMessage(Client.currentSendingMessage);
        if (Client.currentSendingMessage.getFileAddress().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "MESSAGE SENT!");
            alert.show();
        }
    }

    /**
     * this method sends temp message to server
     * @throws IOException
     */
    public void sendTempMessageToServer() throws IOException {
        String tmpToServer = "[" + Client.currentUser.getUsername() + "] [" +
                (repFor == STATES.REPLY ? "reply" : (repFor == STATES.NORMAL_MESSAGE ? "send" : "forward"))
                + "]\n" + "message: [" + subjectFiled.getText() + "] [" + fileAddressText.getText() + "] " +
                (repFor == STATES.FORWARD ? ("from [" + Client.currentSendingMessage.getSender().getUsername() + "] ")
                        : "") + "to [" + Client.currentSendingMessage.getReceiver() + "]\n" +
                "time: [" + thisTime() + "]";
        Client.send.sendTempMessageForServer(tmpToServer);
    }

    /**
     * this method shows alert
     */
    public static void ShowSentMessageWithFileAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "MESSAGE SENT!");
        alert.show();
        return;
    }

    /**
     * this method sets time
     */
    public void setTime() {
        String theTime;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        LocalDateTime now = LocalDateTime.now();
        theTime = dtf.format(now);
        String[] time = theTime.split("-");
        Client.currentSendingMessage.Date = time[0];
        Client.currentSendingMessage.Time = time[1];
        Client.currentSendingMessage.timeToMiliSecond = System.currentTimeMillis();
    }

    /**
     * this method selects file
     * @param mouseEvent
     */
    public void selectFile(MouseEvent mouseEvent) {
        fileTypeVBox.setVisible(true);
        messageText.setVisible(false);
        cancelImage.setVisible(true);
    }

    /**
     * this method shows pictures list
     * @param mouseEvent
     */
    public void showPicturesList(MouseEvent mouseEvent) {
        cancelImage.setVisible(false);
        doneImage.setVisible(true);
        selectFileStackPane.setVisible(true);
        filesListView.setItems(FXCollections.observableArrayList(PicturesList.picturesList));
        filesListView.setCellFactory(userListView -> new ImageListItem());
        filesListView.setVisible(true);
        fileTypeText.setText("Picture");
        fileTypeText.setVisible(false);
    }

    /**
     * this method shows musics list
     * @param mouseEvent
     */
    public void showMusicsList(MouseEvent mouseEvent) {
        cancelImage.setVisible(false);
        fileTypeText.setText("Music");
        fileTypeText.setVisible(false);
        doneImage.setVisible(true);
        selectFileStackPane.setVisible(true);
        filesListView.setItems(FXCollections.observableArrayList(MusicsList.musicsList));
        filesListView.setCellFactory(userListView -> new MusVidListItem());
        filesListView.setVisible(true);
    }

    /**
     * this method shows videos list
     * @param mouseEvent
     */
    public void showVideosList(MouseEvent mouseEvent) {
        cancelImage.setVisible(false);
        fileTypeText.setText("Video");
        fileTypeText.setVisible(false);
        doneImage.setVisible(true);
        selectFileStackPane.setVisible(true);
        filesListView.setItems(FXCollections.observableArrayList(VideosList.videosList));
        filesListView.setCellFactory(userListView -> new MusVidListItem());
        filesListView.setVisible(true);
    }

    /**
     * this method selects a file from list
     * @param mouseEvent
     */
    public void select(MouseEvent mouseEvent) {
        selected = filesListView.getSelectionModel().getSelectedItem();
        fileSelectedImage.setVisible(true);
        selectFileImage.setVisible(false);
        fileAddressText.setText(selected);
    }

    public void done(MouseEvent mouseEvent) {
        fileTypeText.setVisible(true);
        if (fileAddressText.getText().equals(""))
            fileTypeText.setVisible(false);
        doneImage.setVisible(false);
        filesListView.setVisible(false);
        selectFileStackPane.setVisible(false);
        messageText.setVisible(true);
        fileTypeVBox.setVisible(false);
//        fileAddressText.setVisible(true);
//        fileTypeText.setVisible(true);
    }

    /**
     * this method deletes selected file
     * @param mouseEvent
     */
    public void deleteFile(MouseEvent mouseEvent) {
        fileTypeText.setText("");
        fileAddressText.setText("");
        fileSelectedImage.setVisible(false);
        selectFileImage.setVisible(true);
    }

    public void cancelOperation(MouseEvent mouseEvent) {
        fileTypeVBox.setVisible(false);
        messageText.setVisible(true);
        cancelImage.setVisible(false);
        if (fileAddressText.getText().equals(""))
            fileTypeText.setVisible(false);
    }
}
