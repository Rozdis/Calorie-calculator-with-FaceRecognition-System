package faceDetection;


import javafx.application.Application;
import javafx.stage.Stage;
import org.opencv.core.Core;




public class Main extends Application {

    private static String CURRENT_ACCAUNT;
    private static  String PREVIUS_NAME = "  ";
    private static boolean FOUNDED_FACE = true;
    @Override
    public void start(Stage primaryStage)
    {

    }


    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

}

class MatchResult {
    private String matchFileName;
    private double matchDistance;
    public MatchResult(String matchFileName,double matchDistance){
        this.matchFileName=matchFileName;
        this.matchDistance=matchDistance;
    }
    public String getMatchFileName() {
        return matchFileName;
    }
    public void setMatchFileName(String matchFileName) {
        this.matchFileName = matchFileName;
    }
    public double getMatchDistance() {
        return matchDistance;
    }
    public void setMatchDistance(double matchDistance) {
        this.matchDistance = matchDistance;
    }

}

class ImageDistanceInfo {
    int index;
    double value;
    public ImageDistanceInfo(double value, int index) {
        this.value = value;
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    public double getValue() {
        return value;
    }
}





