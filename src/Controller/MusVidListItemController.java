package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Paths;

public class MusVidListItemController {
    public String imageAddress;
    public String MUSIC_IMAGE_URL = "src/Resources/Shapes/icons8-musical-96.png";
    public String VIDEO_IMAGE_URL = "src/Resources/Shapes/icons8-film-reel-96.png";
    @FXML
    ImageView FileTypeImage;
    @FXML
    AnchorPane root;
    @FXML
    Text FileTypeText;
    @FXML
    Text FileAddressText;

    public MusVidListItemController(String imageAddress) throws IOException {
        this.imageAddress = imageAddress;
        load("View/MusVid.fxml", this);
    }

    public AnchorPane init(){
        FileAddressText.setText(imageAddress);
        if(imageAddress.contains("mp3")){
            FileTypeText.setText("MUSIC");
            FileTypeImage.setImage(new Image(Paths.get(MUSIC_IMAGE_URL).toUri().toString()));
        }
        else{
            FileTypeText.setText("VIDEO");
            FileTypeImage.setImage(new Image(Paths.get(VIDEO_IMAGE_URL).toUri().toString()));
        }
        return root;
    }

    public void load(String url, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(url));
        fxmlLoader.setController(controller);
        fxmlLoader.load();
    }
}
