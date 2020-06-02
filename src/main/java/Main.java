import facerec.FaceRecognition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
//Importing javaCv labory
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

import java.io.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

/**
 * that`s all need  to do that is add admin menu(add delete update of information) and finish design
 */

import static facerec.FaceRecognition.debug;
import static facerec.FaceRecognition.getFileExtension;


public class Main extends Application {
    public static final String URL = "jdbc:mysql://localhost:3306/calculator?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
            "&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=utf-8";
    public static final String USER = "root";
    public static final String PASS = "12345";
    public static String CURRENT_ACCAUNT;
    private static  String PREVIUS_NAME = "  ";
    private static String[] NAMES = new String[0];
    private boolean enter = false;
    @Override
    public void start(Stage primaryStage) throws Exception{

        try
        {

            // load the FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sampleDetection.fxml"));

            AnchorPane root = (AnchorPane) loader.load();
            // set a whitesmoke background
            root.setStyle("-fx-background-color: whitesmoke;");
            // create and style a scene
            Scene scene = new Scene(root, 710, 444);
            // create the stage with the given title and the previously created
            // scene
            primaryStage.setTitle("Face Detection and Tracking");
            primaryStage.setScene(scene);
            // show the GUI
            primaryStage.show();

            // init the controller
            Controller controller = loader.getController();
            try {
                new Thread(new Runnable() {
                    public void run() {
                        int counter = 0;
                        while (counter < 3) {
                            controller.init();
                            controller.haarSelected();
                            controller.startCamera(1, "noting here should be");
                            FaceRecognition fr = new FaceRecognition();
                            String directoryName = "gallery"; //directiry where  we will find files
                            String imageToMatch = "probes/face0.png";//if i use own photo - of diff size //that`s face that we should recognize
                            int selectedNumberOfEigenfaces = new Integer(1).intValue();
                            String extension = getFileExtension(imageToMatch);
                            fr.checkCache(directoryName, extension, selectedNumberOfEigenfaces);
                            /////////sometimes here can get exception,but it comment
                           // fr.reconstructFaces(selectedNumberOfEigenfaces);
                            facerec.MatchResult result = fr.findMatchResult(imageToMatch, selectedNumberOfEigenfaces);

                            if (PREVIUS_NAME.equals(result.getMatchFileName())) {
                                counter++;
                            } else {
                                counter = 0;
                            }
                            if (counter == 3) {
                                try {
                                    PREVIUS_NAME = PREVIUS_NAME.replace("2.png", "");
                                    PREVIUS_NAME = PREVIUS_NAME.replace("gallery\\", "");
                                    FileReader reader = new FileReader("Reg_names.txt");
                                    Scanner scann = new Scanner(reader);
                                    while (scann.hasNextLine()) {
                                        if(scann.nextLine().equals(PREVIUS_NAME)) {
                                            enter = true; break;
                                        }
                                        else {
                                            enter = false;
                                        }
                                    }
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }

                                if (enter == false) {
                                    CURRENT_ACCAUNT = "unknown";
                                    Platform.runLater(() -> {
                                        URL url = null;// url for FXMLLoadder otherwise it doesn`t work
                                        try {
                                            url = new File("src/main/java/FXMLfiles/ExceptionInAuntification.fxml").toURI().toURL();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(url);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //Parent root = FXMLLoader.load(getClass().getResource("/FaceDetectionFXML.fxml"));
                                        primaryStage.setTitle("Calories");
                                        primaryStage.setScene(new Scene(root, 600, 450));
                                        primaryStage.show();
                                        controller.setClosed();
                                    });
                                }
                                else {
                                    PREVIUS_NAME = PREVIUS_NAME.replace("2.png", "");
                                    PREVIUS_NAME = PREVIUS_NAME.replace("gallery\\", "");
                                    CURRENT_ACCAUNT = PREVIUS_NAME;
                                    Platform.runLater(() -> {
                                        URL url = null;// url for FXMLLoadder otherwise it doesn`t work
                                        try {
                                            url = new File("src/main/java/FXMLfiles/Main_sample.fxml").toURI().toURL();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(url);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //Parent root = FXMLLoader.load(getClass().getResource("/FaceDetectionFXML.fxml"));
                                        primaryStage.setTitle("Calories");
                                        primaryStage.setScene(new Scene(root, 1200, 900));
                                        primaryStage.show();
                                        controller.setClosed();
                                    });
                                }
                                if (PREVIUS_NAME.equals("gallery\\rozdis2.png")) {
                                    CURRENT_ACCAUNT = "rozdis";
                                    Platform.runLater(() -> {
                                        URL url = null;// url for FXMLLoadder otherwise it doesn`t work
                                        try {
                                            url = new File("src/main/java/FXMLfiles/Admin_panel_main.fxml").toURI().toURL();
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        Parent root = null;
                                        try {
                                            root = FXMLLoader.load(url);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //Parent root = FXMLLoader.load(getClass().getResource("/FaceDetectionFXML.fxml"));
                                        primaryStage.setTitle("Calories");
                                        primaryStage.setScene(new Scene(root, 1200, 900));
                                        primaryStage.show();
                                        controller.setClosed();
                                    });

                                }



                            }
                            PREVIUS_NAME = result.getMatchFileName();
                            System.out.println(PREVIUS_NAME);
                            debug("matches   :" + result.getMatchFileName());

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }catch (Exception e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Dasdasda");
                a.show();
            }

            // set the proper behavior on closing the application
            primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we)
                {
                    controller.setClosed();
                }
            }));
        }
        catch (Exception e)
        {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("dasdsadasdad");
            a.show();
            e.printStackTrace();
        }
    }

    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}


    public static void main(String[] args) {

        launch(args);

    }

}
