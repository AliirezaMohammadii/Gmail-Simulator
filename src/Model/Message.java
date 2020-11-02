package Model;

import java.io.Serializable;

public class Message implements Serializable {
    public String Date;
    public String Time;
    public double timeToMiliSecond;
    public User sender = new User();
    public String receiver = "";
    public String subject = "";
    public String messageText = "";
    public String sendingTime = "";
    public String fileAddress = "";
    public STATES readingState = STATES.UNREAD;
    public STATES starState = STATES.NOT_STARRED;
    public Conversation relatedConversation = new Conversation();
    public boolean isFavorite = false;
    public boolean isRead = false;
    public static String YellowStarURL = "src/Resources/Shapes/icons8-star-96/png";
    public static String colornessStarURL = "src/Resources/Shapes/icons8-star-96(1)/png";
    public static String colornessTickURL = "src/Resources/Shapes/icons8-double-tick-100/png";
    public static String blueStarURL = "src/Resources/Shapes/icons8-double-tick-filled-100/png";

    public Conversation getRelatedConversation() {
        return relatedConversation;
    }

    public void setRelatedConversation(Conversation relatedConversation) {
        this.relatedConversation = relatedConversation;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public STATES getReadingState() {
        return readingState;
    }

    public void setReadingState(STATES readingState) {
        this.readingState = readingState;
    }

    public STATES getStarState() {
        return starState;
    }

    public void setStarState(STATES starState) {
        this.starState = starState;
    }
}
