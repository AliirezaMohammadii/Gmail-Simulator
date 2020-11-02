package Model;

import java.util.ArrayList;

public class VideosList {
    public static ArrayList<String> videosList = new ArrayList<>();
    public static String URL = "src/Resources/Videos/";
    public static void init(){
        videosList.add(URL + "shahriar.mp4");
        videosList.add(URL + "ho3ein.mp4");
        videosList.add(URL + "Abado Yek Rooz 720p_UPTV.co.mkv");
    }
}
