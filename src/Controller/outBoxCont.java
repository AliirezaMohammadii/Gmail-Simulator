package Controller;

import App.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class outBoxCont {

    public ImageView backImage;
    public ListView conversationsList;

    public void initialize(){

    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/mainPage.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }

    public void select(MouseEvent mouseEvent) {
    }
}
