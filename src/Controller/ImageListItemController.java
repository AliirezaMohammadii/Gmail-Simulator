package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageListItemController {

    public String imageAddress;

    @FXML
    public ImageView imageField;
    @FXML
    public AnchorPane root;

    public ImageListItemController(String imageAddress) throws IOException {
        this.imageAddress = imageAddress;
        load("View/imageListItem.fxml", this);
    }

    public AnchorPane init(){
        if (!Files.exists(Paths.get(imageAddress)))
            imageAddress = "src/Resources/images/unKnown.png";
        imageField.setImage(new Image(Paths.get(imageAddress).toUri().toString()));
//        imageField.setClip(new Circle(25, 25, 25));
        return root;
    }

    public void load(String url, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(url));
        fxmlLoader.setController(controller);
        fxmlLoader.load();
    }
}
