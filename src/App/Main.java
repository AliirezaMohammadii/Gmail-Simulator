package App;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static Stage ps;

    public void init(){
        ImagesList.init();
        PicturesList.init();
        VideosList.init();
        MusicsList.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ps = primaryStage;
        Parent root ;
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/entryPage." +
                "fxml")));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Gmail");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        Client.out.writeObject(STATES.FINALIZE.toString());
        Client.out.flush();
        Client.out.writeObject(Client.currentUser.getUsername());
        Client.out.flush();
        Client.out.writeObject(AllFirstMessageSentConversations.list);
        Client.out.flush();
        Client.out.writeObject(AllFirstMessageReceivedConversations.list);
        Client.out.flush();
        System.err.println("lists sent to user in stop method!");
//        Thread.currentThread().wait(1000);
        Client.in.close();
        Client.out.close();
        Client.socket.close();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
