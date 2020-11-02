package Model;

import Controller.ConversationPageController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Conversation implements Serializable {
    public ArrayList<Message> messageList = new ArrayList<>();
    public String firstMessageSender;
    public String otherSide;
    public String conversationSubject = "";
    public boolean haveUnReadMessage = true;
    public boolean haveStarredMessage = false;
    public int numOfStarredMessaged = 0;
    public String IMAGE_URL = "";


    public String getFirstMessageSender() {
        return firstMessageSender;
    }

    public void setFirstMessageSender(String firstMessageSender) {
        this.firstMessageSender = firstMessageSender;
    }

    public String getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(String otherSide) {
        this.otherSide = otherSide;
    }

    public String getConversationSubject() {
        return conversationSubject;
    }

    public void setConversationSubject(String conversationSubject) {
        this.conversationSubject = conversationSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return conversationSubject.equals(that.conversationSubject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationSubject);
    }
}
