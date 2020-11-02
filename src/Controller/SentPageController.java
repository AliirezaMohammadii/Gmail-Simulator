package Controller;

import App.Main;
import Model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class SentPageController {
    public Conversation selected;
    public ListView<Conversation> conversationsList;
    public ImageView backImage;
    public TextField searchField;
    public Text searchByTextField;
    public Text searchByEmailAddressField;
    public Text searchBySubjectField;
    public STATES searchState;
    Comparator<Conversation> comp = new Comparator<>() {
        public int compare(Conversation c1, Conversation c2) {
            return Double.compare(c1.messageList.get(c1.messageList.size() - 1).timeToMiliSecond,
                    c2.messageList.get(c2.messageList.size() - 1).timeToMiliSecond) * -1;
        }
    };

    public void initialize() {
        ArrayList<Conversation> reversedList = AllFirstMessageSentConversations.list;
        reversedList.sort(comp);
        reversedList = filteredListByBlockedUsers(reversedList);
        conversationsList.setItems(FXCollections.observableArrayList(reversedList));
        conversationsList.setCellFactory(userListView -> new ConversationListItem());
    }

    public void select(MouseEvent mouseEvent) throws IOException {
//        selected = conversationsList.getSelectionModel().getSelectedItem();
//        Client.currentConversation = selected;
//        ConversationPageController.lastPage = STATES.SENT_PAGE;
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/ConversationPage.fxml")));
//        Main.ps.setScene(new Scene(root));
//        return;
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/mainPage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void updateList(MouseEvent mouseEvent) {
        ArrayList<Conversation> mainList = AllFirstMessageSentConversations.list;
        mainList.sort(comp);
        mainList = filteredListByBlockedUsers(mainList);
        conversationsList.setItems(FXCollections.observableArrayList(mainList));
        conversationsList.setCellFactory(userListView -> new ConversationListItem());
    }

    public void search(MouseEvent mouseEvent) {
        searchByEmailAddressField.setVisible(true);
        searchBySubjectField.setVisible(true);
        searchByTextField.setVisible(true);
        searchField.setVisible(false);
        if (searchField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "NOTHING ENTERED TO SEARCH!!!");
            alert.show();
            return;
        } else {
            ArrayList<Conversation> newList = new ArrayList<>();
            if (searchState == STATES.SEARCH_BY_SUBJECT) {
                for (Conversation conversation : AllFirstMessageSentConversations.list) {
                    if (conversation.getConversationSubject().toLowerCase().
                            startsWith(searchField.getText().toLowerCase())) {
                        newList.add(conversation);
                    }
                }
            } else {
                String userName = searchField.getText();
                if (searchField.getText().contains("@gmail.com")) {
                    userName = "";
                    for (int i = 0; !(searchField.getText().substring(i).equals("@gmail.com")); i++) {
                        userName += searchField.getText().charAt(i);
                    }
                }
                for (Conversation conversation : AllFirstMessageSentConversations.list) {
                    if (conversation.getFirstMessageSender().toLowerCase().
                            startsWith(userName.toLowerCase())) {
                        newList.add(conversation);
                    }
                }
            }
            newList.sort(comp);
            newList = filteredListByBlockedUsers(newList);
            conversationsList.setItems(FXCollections.observableArrayList(newList));
            conversationsList.setCellFactory(userListView -> new ConversationListItem());
        }
    }

    public void searchByEmailAddressAction(MouseEvent mouseEvent) {
        searchByEmailAddressField.setVisible(false);
        searchBySubjectField.setVisible(false);
        searchByTextField.setVisible(false);
        searchState = STATES.SEARCH_BY_EMAILADDRESS;
        searchField.setVisible(true);
    }

    public void searchBySubjectAction(MouseEvent mouseEvent) {
        searchByEmailAddressField.setVisible(false);
        searchBySubjectField.setVisible(false);
        searchByTextField.setVisible(false);
        searchState = STATES.SEARCH_BY_SUBJECT;
        searchField.setVisible(true);
    }

    public void updateListByUnReadConversations(MouseEvent mouseEvent) {
        ArrayList<Conversation> theList = new ArrayList<>(AllFirstMessageSentConversations.list);
        theList.sort(comp);
        ArrayList<Conversation> unReadConversationList = new ArrayList<>();
        for (int i = theList.size() - 1; i >= 0; i--) {
            if (theList.get(i).haveUnReadMessage) {
                unReadConversationList.add(theList.get(i));
                theList.remove(i);
            }
        }
        Collections.reverse(unReadConversationList);
        ArrayList<Conversation> mainList = new ArrayList<>();
        mainList.addAll(unReadConversationList);
        mainList.addAll(theList);
        mainList = filteredListByBlockedUsers(mainList);
        conversationsList.setItems(FXCollections.observableArrayList(mainList));
        conversationsList.setCellFactory(userListView -> new ConversationListItem());
    }

    public ArrayList<Conversation> filteredListByBlockedUsers(ArrayList<Conversation> list) {
        ArrayList<Conversation> newList = new ArrayList<>(list);
        for (int i = newList.size() - 1; i >= 0; i--) {
            if (Client.currentUser.blockedUsersList.contains(newList.get(i).getFirstMessageSender())
                    || Client.currentUser.blockedUsersList.contains(newList.get(i).getOtherSide())) {
                newList.remove(i);
            }
        }
        return newList;
    }
}
