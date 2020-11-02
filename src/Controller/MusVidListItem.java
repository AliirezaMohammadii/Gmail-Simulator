package Controller;

import javafx.scene.control.ListCell;

import java.io.IOException;

public class MusVidListItem extends ListCell<String> {
    @Override
    public void updateItem(String address, boolean empty) {
        super.updateItem(address, empty);
        if (address != null) {
//            setStyle("-fx-background-color: #adb4bc");
            try {
                setGraphic
                        (new MusVidListItemController(address).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
