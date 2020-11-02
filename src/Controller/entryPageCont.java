package Controller;

import App.Main;
import Model.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class entryPageCont {

    public TextField serverIPField;
    public TextField ip2;

    public void enter(MouseEvent mouseEvent) throws IOException {
        Client.IP = serverIPField.getText();
        Client.ServerIP = ip2.getText();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("View/SignIn.fxml")));
        Main.ps.setScene(new Scene(root));
        return;
    }
}
