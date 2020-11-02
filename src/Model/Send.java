package Model;

import Controller.sendMessageController;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.FileInputStream;
import java.io.IOException;

public class Send {

//    @Override
//    public void run() {
//        try {
//            send();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void send() throws Exception {
//        while (true) {
//            while (Client.state == STATES.NO_REQUEST) {
//                Thread.sleep(2000);
//                System.err.println("waiting...");
//            }
//            Client.out.writeObject(Client.state.toString());
//            Client.out.flush();
//            STATES request = Client.state;
//            switch (request){
//                case SIGN_UP:{
//                    if (checkSignUpRequest()) {
//                        System.out.println("\nClient Signed Up");
//                    }
//                }break;
//                case SIGN_IN:{
//                    if (checkSignInRequest()) {
//                        System.out.println("\nClient Signed In");
//                    }
//                }break;
//                case SEND_MESSAGE:{
////                    sendMessage(new Message());
//                }
//
//            }
//            Client.state = STATES.NO_REQUEST;
//        }
//    }

    public void checkSignInRequest() throws IOException, ClassNotFoundException {
        Client.out.writeObject(Client.state.toString());
        Client.out.flush();
        String[] inf = new String[2];
        inf[0] = Client.SignIn_Username.toLowerCase();
        inf[1] = Client.SignIn_Password;
        Client.out.writeObject(inf);
        Client.out.flush();
//        Client.state = STATES.valueOf((String) Client.in.readObject());
//        if (Client.state == STATES.USER_SIGNED_IN) {
//            String[] currentInformation = (String[]) Client.in.readObject();
//            setUserInformation(currentInformation);
//            System.out.println("User Signed In!");
//            return true;
//        } else if (Client.state == STATES.WRONG_PASSWORD) {
////            Platform.runLater(
//                    SignInCont::wrongPasswordAlert
//            );
//            Client.state = STATES.NO_REQUEST;
//            return false;
//        } else {
////            Platform.runLater(
////                    SignInCont::notFoundUsername
////            );
////            Client.state = STATES.NO_REQUEST;
//            return false;
//        }
    }

//    public void setUserInformation(String[] currentInformation) {
//        Client.currentUser.setUsername(Client.SignIn_Username);
//        Client.currentUser.setPassword(Client.SignIn_Password);
//        Client.currentUser.setFirstName(currentInformation[0]);
//        Client.currentUser.setLastName(currentInformation[1]);
//        Client.currentUser.setBirthDay(currentInformation[2]);
//        Client.currentUser.setBirthMonth(currentInformation[3]);
//        Client.currentUser.setBirthYear(currentInformation[4]);
//    }

    public void checkSignUpRequest() throws IOException, ClassNotFoundException {
        Client.out.writeObject(Client.state.toString());
        Client.out.flush();
        System.out.println("Sign Up Request Sent ...");
        Client.out.writeObject(Client.currentUser.getUsername().toLowerCase());
        Client.out.flush();
        System.out.println("\nClient Sent Username To Check");
//        Client.state = STATES.valueOf((String) Client.in.readObject());
//        System.out.println("\nServer answer : " + Client.state.toString());
//        if (Client.state == STATES.REPETITIVE_USERNAME) {
////            Platform.runLater(
////                    SignUpCont::showRepetitiveUsernameAlert
////            );
//////            Client.state = STATES.NO_REQUEST;
////            System.out.println("\nusername is Repetitive!");
//            return false;
//        }
//        sendInfToServer();
//        System.out.println("\nClient Sent Information To Server");
//        return true;

    }

    public void sendInfToServer() throws IOException {
        Object[] userInformation = new Object[9];
        userInformation[0] = Client.currentUser.getPassword();
        userInformation[1] = Client.currentUser.getFirstName();
        userInformation[2] = Client.currentUser.getLastName();
        userInformation[3] = Client.currentUser.getBirthYear();
        userInformation[4] = Client.currentUser.getBirthMonth();
        userInformation[5] = Client.currentUser.getBirthDay();
        userInformation[6] = Client.currentUser.getProfileImageURL();
        userInformation[7] = Client.currentUser.getPhoneNumber();
        userInformation[8] = Client.currentUser.getSexuality();
        Client.out.writeObject(userInformation);
        Client.out.flush();
        sendImageToServer();
    }

    public void sendImageToServer() throws IOException {
//        DataOutputStream outt = new DataOutputStream(Client.socket.getOutputStream());
        FileInputStream fileInputStream = new FileInputStream(Client.currentUser.getProfileImageURL());
        int readBytes;
        byte[] buffer = new byte[2048];
        while ((readBytes = fileInputStream.read(buffer)) > 0) {
            Client.out.write(buffer, 0, readBytes);
            Client.out.flush();
        }
//        Client.out.close();
//        outt.flush();
//        outt.close();
//        Client.out.close();
//        Client.out = new ObjectOutputStream(Client.socket.getOutputStream());
        fileInputStream.close();
        System.out.println("image Sent!");
        Client.disConnect();
        Client.connect();
    }

    public void sendMessage(Message message) throws IOException {
        Client.out.writeObject(Client.state.toString());
        Client.out.flush();
//        System.out.println("user sent \" message request\" ");
        Client.out.writeObject(message.getReceiver());
        Client.out.flush();
//        System.out.println("receiver username sent to check existion!");
    }

    public void SendMessage(Message message) throws IOException {
//        boolean allowedToCreateNew
//        for (Conversation conversation : AllFirstMessageSentConversations.list) {
//            message.getSubject().equals(conversation.getConversationSubject()){
//
//            }
//        }

//        System.out.println("entry to mainSend message method!");
//        Client.out.writeObject(message);
//        Client.out.flush();

//        if(!message.getFileAddress().equals("")){
//
//        }
        System.out.println("message Sent!");
        boolean halle = false;
//        System.err.println("Size of AllFirstMessageSentConversations List : " +
//                AllFirstMessageSentConversations.list.size());
        for (Conversation conversation : AllFirstMessageSentConversations.list) {
//            System.err.println("subjects equality :\n" + conversation.getConversationSubject()
//                    + " == " + Client.currentSendingMessage.getRelatedConversation().getConversationSubject());
            if (conversation.getConversationSubject().equals
                    (Client.currentSendingMessage.getRelatedConversation().getConversationSubject())) {
                halle = true;
                conversation.messageList.add(Client.currentSendingMessage);
            }
        }
//        System.err.println("Size of AllFirstMessageReceivedConversations List : " +
//                AllFirstMessageReceivedConversations.list.size());
        if(!halle) {
            for (Conversation conversation : AllFirstMessageReceivedConversations.list) {
                halle = true;
//            System.err.println("subjects equality :\n" + conversation.getConversationSubject()
//                    + " == " + Client.currentSendingMessage.getRelatedConversation().getConversationSubject());
                if (conversation.getConversationSubject().equals
                        (Client.currentSendingMessage.getRelatedConversation().getConversationSubject())) {
                    conversation.messageList.add(Client.currentSendingMessage);
                }
            }
        }
        if(!halle) {
            createNewConversation();
        }

        Object[] messageFields = new Object[19];
        messageFields[0] = Client.currentSendingMessage.Date;
        messageFields[1] = Client.currentSendingMessage.Time;
        messageFields[2] = Client.currentSendingMessage.timeToMiliSecond;
        messageFields[3] = Client.currentSendingMessage.getSender().getUsername();
        messageFields[4] = Client.currentSendingMessage.getSender().getFirstName();
        messageFields[5] = Client.currentSendingMessage.getSender().getLastName();
        messageFields[6] = Client.currentSendingMessage.getReceiver();
        messageFields[7] = Client.currentSendingMessage.getSubject();
        messageFields[8] = Client.currentSendingMessage.getMessageText();
        messageFields[9] = Client.currentSendingMessage.getSendingTime();
        messageFields[10] = Client.currentSendingMessage.getFileAddress();
        messageFields[11] = Client.currentSendingMessage.sender.getProfileImageURL();
        messageFields[12] = Client.currentSendingMessage.getRelatedConversation().getFirstMessageSender();
        messageFields[13] = Client.currentSendingMessage.getRelatedConversation().getOtherSide();
        messageFields[14] = Client.currentSendingMessage.getRelatedConversation().getConversationSubject();
        messageFields[15] = Client.currentSendingMessage.getRelatedConversation().haveUnReadMessage;
        messageFields[16] = Client.currentSendingMessage.getRelatedConversation().haveStarredMessage;
        messageFields[17] = Client.currentSendingMessage.getRelatedConversation().numOfStarredMessaged;
        messageFields[18] = Client.currentSendingMessage.getRelatedConversation().IMAGE_URL;
        System.out.println(Client.currentSendingMessage.getRelatedConversation().IMAGE_URL);

        Client.out.writeObject(messageFields);
        Client.out.flush();

        if(Client.currentSendingMessage.getFileAddress().equals("")){
            Client.wait = false;
            Client.out.writeObject(STATES.MESSAGE_WITHOUT_FILE.toString());
            Client.out.flush();
        }
        else{
            Client.out.writeObject(STATES.MESSAGE_WITH_FILE.toString());
            Client.out.flush();
            Client.out.writeObject(Client.currentUser.getUsername());
            Client.out.flush();
            Client.out.writeObject(Client.currentSendingMessage.getFileAddress());
            Client.out.flush();
            sendMessageFileToServer(Client.currentSendingMessage.getFileAddress());
//            Task<Void> sendFileTask = new Task<Void>() {
//                @Override
//                protected Void call() throws Exception {
//                    Client.send.sendMessageFileToServer(Client.currentSendingMessage.getFileAddress());
//                    return null;
//                }
//            };
//            new Thread(sendFileTask).start();
        }
        Client.currentSendingMessage = new Message();



//        if(Client.currentSendingMessage.getRelatedConversation().getConversationSubject().equals("")){
//            createNewConversation();
//        }
//        else{
//            addMessageToRelatedConversation();
//        }
    }

//    public void addMessageToRelatedConversation() {
//        for (Conversation conversation : AllFirstMessageSentConversations.list) {
//            if(conversation.equals(Client.currentSendingMessage.getRelatedConversation())){
//                conversation.messageList.add(Client.currentSendingMessage);
//                return;
//            }
//        }
//    }

    public void sendMessageFileToServer(String fileAddress) throws IOException {
        /////
//        Client.wait = true;
        /////
        System.err.println("sender started to send file to server!");
        FileInputStream fileInputStream = new FileInputStream(fileAddress);
        int readBytes;
        byte[] buffer = new byte[2048];
        while ((readBytes = fileInputStream.read(buffer)) > 0) {
            System.err.println("entry to loop");
            Client.out.write(buffer, 0, readBytes);
            Client.out.flush();
//            System.err.println("sender is handling file");
        }
        fileInputStream.close();
        System.err.println("file Sent!");
        //                        SignUpCont.showRestOfInfPage();
        Platform.runLater(
                sendMessageController::ShowSentMessageWithFileAlert
        );
        Client.disConnect();
        Client.connect();
    }

    public void createNewConversation() {
        Conversation newConversation = new Conversation();
        newConversation.IMAGE_URL = Client.currentUser.getProfileImageURL();
        newConversation.setFirstMessageSender(Client.currentUser.getUsername());
        newConversation.setOtherSide(Client.currentSendingMessage.getReceiver());
        newConversation.setConversationSubject(Client.currentSendingMessage.getSubject());
        newConversation.messageList.add(Client.currentSendingMessage);
        Client.currentSendingMessage.setRelatedConversation(newConversation);
        AllFirstMessageSentConversations.list.add(newConversation);
    }

    public void sendTempMessageForServer(String tmp) throws IOException {
        Client.out.writeObject(STATES.TEMP_PM.toString());
        Client.out.flush();
        Client.out.writeObject(tmp);
        Client.out.flush();
    }

}