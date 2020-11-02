package Model;


import javafx.scene.control.ListCell;

import java.io.IOException;
import java.util.ArrayList;

public class ImagesList {
    public static ArrayList<String> imagesList = new ArrayList<>();
    public static String URL = "src/Resources/images/";
    public static void init() {
//        imagesList.add(URL + "unKnown.png");
        imagesList.add(URL + "TomCruise.jpg");
        imagesList.add(URL + "RobertDowneyJr.jpg");
        imagesList.add(URL + "JohnnyDepp.jpg");
        imagesList.add(URL + "ChrisEvans.jpg");
        imagesList.add(URL + "TomHolland.jpg");
        imagesList.add(URL + "BradPit.jpg");
    }

}
