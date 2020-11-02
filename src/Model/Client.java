package Model;

import Controller.SignInCont;
import Controller.SignUpCont;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Client {
    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static Receive receive;
    public static Send send;
    public static User currentUser = new User();
    public static STATES state = STATES.SIGN_IN;
    public static String SignIn_Username;
    public static String SignIn_Password;
    public static Message currentSendingMessage = new Message();
    public static Message currentReceivedMessage = new Message();
    public static Conversation currentConversation = new Conversation();
    public static String ServerIP;
    public static boolean wait = false;
    public static boolean firstErrorMessage = true;
    public static String sentFileAddress = "";
    public static String receivedFileAddress = "";
    public static String IP;

    public static void start() throws IOException {
        socket = new Socket(IP, Integer.parseInt(ServerIP));
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
        // true : firstConnection
        out.writeObject(true);
        send = new Send();
        receive = new Receive();
//        new Thread(send).start();
        new Thread(receive).start();
    }

    public static void disConnect() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public static void connect() throws IOException {
        socket = new Socket(IP, Integer.parseInt(ServerIP));
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        wait = false;
        out.writeObject(false);
        out.writeObject(Client.currentUser.getUsername());
    }
}

class Receive implements Runnable {

    public void handle() throws IOException, ClassNotFoundException, InterruptedException {
        while (true) {
            while (Client.wait) {
                Thread.sleep(1000);
                System.err.println("waiting...");
            }
            Client.state = (STATES.valueOf((String) Client.in.readObject()));
            System.out.println(Client.state.toString());
            outer:
            switch (Client.state) {
                case USER_SIGNED_IN: {
                    Object[] currentInformation = (Object[]) Client.in.readObject();
                    setUserInformation(currentInformation);

                    AllFirstMessageSentConversations.list = (ArrayList<Conversation>) Client.in.readObject();
                    AllFirstMessageReceivedConversations.list = (ArrayList<Conversation>) Client.in.readObject();
                    if(AllFirstMessageSentConversations.list == null){
                        System.out.println("AllFirstMessageSentConversationsList is null!!!");
                        AllFirstMessageSentConversations.list = new ArrayList<>();
                    }
                    if(AllFirstMessageReceivedConversations.list == null){
                        System.out.println("AllFirstMessageSentConversationsList is null!!!");
                        AllFirstMessageReceivedConversations.list = new ArrayList<>();
                    }
                    System.err.println("sentList and inboxList received from Server");

//                    System.out.println("User Signed In!");
                    Platform.runLater(
                            () -> {
                                try {
                                    SignInCont.showMainPanelPage();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                }
                break;
                case SIGN_IN:
                    break;
                case SIGN_UP:
                    break;
                case WRONG_PASSWORD: {
                    Platform.runLater(
                            SignInCont::wrongPasswordAlert
                    );
                }
                break;
                case USER_NOT_FOUND: {
                    // when user sign ih=n and there is not entry username in server users' list
                    Platform.runLater(
                            SignInCont::notFoundUsername
                    );
                }
                break;
                case REPETITIVE_USERNAME: {
                    Platform.runLater(
                            SignUpCont::showRepetitiveUsernameAlert
                    );
//                    System.out.println("\nusername is Repetitive!");
                }
                break;
                case USER_SIGNED_UP: {
//                    System.out.println("\nUser Signed Up");
//                    System.out.println("\nUser Sent Information To Server...");
                    Platform.runLater(
                            () -> {
                                try {
                                    SignUpCont.showRestOfInfPage();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                    Client.wait = true;
                }
                break;
                case NO_REQUEST:
                    break;
                case STARTED:
                    break;
                case READED:
                    break;
                case UNREAD:
                    break;
                case STARRED:
                    break;
                case NOT_STARRED:
                    break;
                case SEND_MESSAGE:
                    break;
                case USER_EXISTS: {
//                    System.out.println("receiver address found!");
//                    System.out.println("ready to send message!");
                    Client.wait = true;
                    Client.send.SendMessage(Client.currentSendingMessage);
                }
                break;
                case USER_NOT_EXISTS:
                    break;
                case NEW_MESSAGE: {
//                    System.out.println("new message pm received!!!");
                    String unavailableAddress = (String) Client.in.readObject();
                    System.out.println("unavailable address received!");
//                    System.out.println();
//                    System.out.println(unavailableAddress + "////");
                    if (!unavailableAddress.equals("")) {
//                        System.out.println("error message received!");
                        createTempMessage(unavailableAddress);
                    } else {

                        Object[] messageFileds = new Object[19];
                        messageFileds = (Object[]) Client.in.readObject();
                        Message message = new Message();
                        message.Date = (String) messageFileds[0];
                        message.Time = (String) messageFileds[1];
                        message.timeToMiliSecond = (double) messageFileds[2];
                        message.sender.username = (String) messageFileds[3];
                        message.sender.firstName = (String) messageFileds[4];
                        message.sender.lastName = (String) messageFileds[5];
                        message.receiver = (String) messageFileds[6];
                        message.subject = (String) messageFileds[7];
                        message.messageText = (String) messageFileds[8];
                        message.sendingTime = (String) messageFileds[9];
                        message.fileAddress = (String) messageFileds[10];
                        message.sender.profileImageURL = (String) messageFileds[11];
//                        System.out.println("okeye seyyed");
                        message.relatedConversation.firstMessageSender = (String) messageFileds[12];
                        message.relatedConversation.otherSide = (String) messageFileds[13];
                        message.relatedConversation.conversationSubject = (String) messageFileds[14];
                        message.relatedConversation.haveUnReadMessage = (boolean) messageFileds[15];
                        message.relatedConversation.haveStarredMessage = (boolean) messageFileds[16];
                        message.relatedConversation.numOfStarredMessaged = (int) messageFileds[17];
                        message.relatedConversation.IMAGE_URL = (String) messageFileds[18];


                        Client.currentReceivedMessage = message;
                        Client.currentReceivedMessage.getRelatedConversation().IMAGE_URL = (String) messageFileds[18];
                        System.err.println("besalamatiiiiiiii message received!");


                        System.out.println("message received with out file!");
                        sendTempMessageToServer();
                        if (!Client.currentReceivedMessage.getFileAddress().equals("")) {

                            String smalledFileAddress = "";
                            boolean ok = false;
                            for (int i = 0; i < Client.currentReceivedMessage.getFileAddress().length(); i++) {
                                if (!ok) {
                                    if (Client.currentReceivedMessage.getFileAddress().substring(i).startsWith("Pictures")
                                            || Client.currentReceivedMessage.getFileAddress().substring(i).startsWith("Videos") ||
                                            Client.currentReceivedMessage.getFileAddress().substring(i).startsWith("Musics")) {
                                        while (Client.currentReceivedMessage.getFileAddress().charAt(i) != '/') {
                                            i++;
                                        }
                                        i++;
                                        ok = true;
                                    }
                                }
                                if (ok) {
                                    smalledFileAddress += Client.currentReceivedMessage.getFileAddress().charAt(i);
                                }
                            }

                            Client.receivedFileAddress = "Message ExChanges/(" +
                                    Client.currentReceivedMessage.getSender().getUsername() + ") TO (" +
                                    Client.currentReceivedMessage.getReceiver() + ")/" +
                                    smalledFileAddress;
                            File file = new File("Message ExChanges/(" +
                                    Client.currentReceivedMessage.getSender().getUsername() + ") TO (" +
                                    Client.currentReceivedMessage.getReceiver() + ")");
//                            System.err.println("File file = new ......");
                            if (!file.exists()) {
                                file.mkdir();
                            }
//                            System.err.println("file mkdir shod :)))");
//                            System.out.println("FILE ADDRESS : " + Client.currentReceivedMessage.getFileAddress());
                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    "Message ExChanges/(" +
                                            Client.currentReceivedMessage.getSender().getUsername() + ") TO (" +
                                            Client.currentReceivedMessage.getReceiver() + ")/" +
                                            smalledFileAddress);
                            int readBytes;
                            byte[] buffer = new byte[2048];
                            System.err.println("Client started to receive file from server!");
                            while ((readBytes = Client.in.read(buffer)) > 0) {
//                                System.err.println("entry to loop");
                                fileOutputStream.write(buffer, 0, readBytes);
                                fileOutputStream.flush();
//                                System.err.println("receiver is handling file");
                            }
                            ////
//                            Client.wait = true;
                            System.err.println("file received!");
                            ////
                            Client.disConnect();
                            Client.connect();
                        }

//                        System.out.println("message Received!");
                        System.err.println(Client.currentReceivedMessage.getSender().getUsername()
                                + " : " + Client.currentReceivedMessage.getMessageText());
//                        System.err.println("Size of AllFirstMessageReceivedConversations List : " +
//                                AllFirstMessageReceivedConversations.list.size());
                        for (Conversation conversation : AllFirstMessageReceivedConversations.list) {
//                            System.err.println("subjects equality :\n" + conversation.getConversationSubject()
//                                    + " == " + Client.currentReceivedMessage.getRelatedConversation().
//                                    getConversationSubject());
                            if (conversation.getConversationSubject().equals(Client.currentReceivedMessage.
                                    getRelatedConversation().getConversationSubject())) {
                                conversation.messageList.add(Client.currentReceivedMessage);
                                Client.currentReceivedMessage = new Message();
                                break outer;
                            }
                        }
//                        System.err.println("Size of AllFirstMessageSentConversations List : " +
//                                AllFirstMessageSentConversations.list.size());
                        for (Conversation conversation : AllFirstMessageSentConversations.list) {
//                            System.err.println("subjects equality :\n" + conversation.getConversationSubject()
//                                    + " == " + Client.currentReceivedMessage.getRelatedConversation().
//                                    getConversationSubject());
                            if (conversation.getConversationSubject().equals(Client.currentReceivedMessage.
                                    getRelatedConversation().getConversationSubject())) {
                                conversation.messageList.add(Client.currentReceivedMessage);
                                Client.currentReceivedMessage = new Message();
                                break outer;
                            }
                        }
                        System.out.println("new Conversation is ready to be created!");
                        createNewConversation(messageFileds);
//                        if (Client.currentReceivedMessage.getRelatedConversation().getConversationSubject().equals("")) {
//                            System.out.println("new Conversation is ready to be created!");
//                            System.err.println(Client.currentReceivedMessage.getSender().getUsername()
//                                    + " : " + Client.currentReceivedMessage.getMessageText());
//                            createNewConversation();
//                        } else {
//                            addMessageToRelatedConversation();
//                            break outer;
//                        }
                    }
                }
                break;
                case SENT_PAGE:
                    break;
                case INBOX_PAGE:
                    break;
                case MAIN_PAGE:
                    break;
                case CONVERSATION_PAGE:
                    break;
                case REPLY:
                    break;
                case FORWARD:
                    break;
                case CHANGE_FIRST_NAME:
                    break;
                case CHANGE_LAST_NAME:
                    break;
                case CHANGE_PASSWORD:
                    break;
                case CHANGE_BORN:
                    break;
                case CHANGE_PHONENUMBER:
                    break;
                case CHANGE_SEXUALITY:
                    break;
                case CHANGE_PROFILE_IMAGE:
                    break;
                case SEARCH_BY_SUBJECT:
                    break;
                case SEARCH_BY_EMAILADDRESS:
                    break;
                case MESSAGE_WITH_FILE:
                    break;
                case MESSAGE_WITHOUT_FILE:
                    break;
                case TEMP_PM:
                    break;
                case NORMAL_MESSAGE:
                    break;
                case FINALIZE:
                    break;
            }
        }
    }

    /**
     *
     * @throws IOException it sends temp message to server
     */
    public void sendTempMessageToServer() throws IOException {
        String tempForServer = "[" + Client.currentUser.getUsername() + "] receive\n" +
                "message: [" + Client.currentReceivedMessage.getSender().getUsername()
                + "] [" + Client.currentReceivedMessage.getFileAddress() + "]\n" +
                "time: [" + thisTime() + "]";
//        Client.send.sendTempMessageForServer(tempForServer);
    }

//    public void addMessageToRelatedConversation() {
//        for (Conversation conversation : AllFirstMessageReceivedConversations.list) {
//            if (conversation.equals(Client.currentReceivedMessage.getRelatedConversation())) {
//                conversation.messageList.add(Client.currentReceivedMessage);
//                return;
//            }
//        }
//    }

    /**
     * this method sets message time
     * @return
     */
    public static String thisTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * this method creates conversations
     * @param msg
     */
    public void createNewConversation(Object[] msg) {
        Conversation newConversation = new Conversation();
        newConversation.setFirstMessageSender((String) msg[12]);
        newConversation.IMAGE_URL = (String) msg[18];
        newConversation.setOtherSide((String) msg[13]);
        newConversation.setConversationSubject((String) msg[14]);
        newConversation.messageList.add(Client.currentReceivedMessage);
        Client.currentReceivedMessage.setRelatedConversation(newConversation);
        AllFirstMessageReceivedConversations.list.add(newConversation);
        Client.currentReceivedMessage = new Message();
    }

    /**
     * this method send error message to current user
     * @param unavailableAddress
     */
    public void createTempMessage(String unavailableAddress) {
        Client.currentReceivedMessage = new Message();
        String theTime;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        LocalDateTime now = LocalDateTime.now();
        theTime = dtf.format(now);
        Client.currentReceivedMessage.Date = theTime.split("-")[0];
        Client.currentReceivedMessage.Time = theTime.split("-")[1];
        Client.currentReceivedMessage.setMessageText(" There is not any user with\n\" "
                + unavailableAddress + "@gmail.com \" Address !");
        Client.currentReceivedMessage.setFileAddress(null);
        Client.currentReceivedMessage.setSubject("UnAvailable Address Error");
        User temp = new User();
        temp.setFirstName("Mail Delivery Subsystem");
        temp.setUsername("mailerdaemon@googlemail.com");
        Client.currentReceivedMessage.setSender(temp);
        if (Client.firstErrorMessage) {
            Conversation errorConversation = new Conversation();
            errorConversation.setFirstMessageSender(Client.currentReceivedMessage.getSender().getUsername());
            errorConversation.setOtherSide(Client.currentUser.getUsername());
            errorConversation.setConversationSubject(Client.currentReceivedMessage.getSubject());
            errorConversation.messageList.add(Client.currentReceivedMessage);
            Client.currentReceivedMessage.setRelatedConversation(errorConversation);
            AllFirstMessageReceivedConversations.list.add(errorConversation);
            Client.firstErrorMessage = false;
        } else {
            for (Conversation conversation : AllFirstMessageReceivedConversations.list) {
                if (conversation.getConversationSubject().equals(Client.currentReceivedMessage.getSubject())) {
                    conversation.messageList.add(Client.currentReceivedMessage);
                    break;
                }
            }
        }
    }

    /**
     * this method sends user information to server
     * @param currentInformation
     */
    public void setUserInformation(Object[] currentInformation) {
        Client.currentUser.setUsername(Client.SignIn_Username);
        Client.currentUser.setPassword(Client.SignIn_Password);
        Client.currentUser.setFirstName((String) currentInformation[0]);
        Client.currentUser.setLastName((String) currentInformation[1]);
        Client.currentUser.setBirthDay((String) currentInformation[2]);
        Client.currentUser.setBirthMonth((String) currentInformation[3]);
        Client.currentUser.setBirthYear((String) currentInformation[4]);
        Client.currentUser.setProfileImageURL((String) currentInformation[5]);
        Client.currentUser.setPhoneNumber((String) currentInformation[6]);
        Client.currentUser.setSexuality((String) currentInformation[7]);
    }


    @Override
    public void run() {
        try {
            handle();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}