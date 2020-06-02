import faceDetection.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;

import static org.opencv.core.CvType.CV_8UC3;


public class Controller extends Main{
    private static ResultSet rs;
    private static Statement st;
    Connection connection;
    public String login;
    public String newLogin, gender;
    public int age, target, activity, typeOfFood;
    public float weight, height;
    //initialize colums for our table of food

    @FXML
    private TextField inputText, outName, outCal, outProt, outFats, outCarb;
    @FXML
    private TextField inputNewLogin, inputAge, inputWeight, inputHeight, LoginOfManager, AddProductName, AddProductCalories, AddProductProt, AddProductFats, AddProductCarb;
    @FXML
    private TextField inputFrom, inputTo;
    @FXML
    private ToggleGroup RadioButton;
    @FXML
    private TextArea NormalCaloriesText, NormalCaloriesText2;
    @FXML
    private Label outputText;
    @FXML
    private Label loginTextProfile, targetTextProfile, activityTextProfile, ageTextProfile, weightTextProfile, heightTextProfile, genderTextProfile;
    @FXML
    public Button OpenRegButton, EnterButton, OpenMainButton, button, profileButton, hideButton, filtrButton;
    @FXML
    public Button AddProductButton, RecomandationButtonInMain, EatenFoodButton, LogOutButton;
    @FXML
    public MenuButton menuButtonTypeOfFood;
    @FXML
    private TableView<Food> tableFood;
    @FXML
    private TableColumn<Food, Integer> idColum;
    @FXML
    private TableColumn<Food, String> nameColum;
    @FXML
    private  TableColumn<Food, Integer> CaloriesColum;
    @FXML
    private TableColumn<Food, Float> proteinsColum;
    @FXML
    private TableColumn<Food,Float> fatsColum;
    @FXML
    private TableColumn<Food,Float> carbohydratesColum;
    private   ObservableList<Food> foodData = FXCollections.observableArrayList();
    // FXML buttons
    @FXML
    private Button cameraButton;
    // the FXML area for showing the current frame
    @FXML
    private ImageView originalFrame;
    // checkboxes for enabling/disabling a classifier
    @FXML
    private CheckBox haarClassifier;
    @FXML
    private CheckBox lbpClassifier;
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;
    // face cascade classifier
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize, idOfCurrentProduct;



    @FXML
    private void ClickOnEnter() throws IOException {
        login = inputText.getText();

        try {
            //connect to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();
            //statement helps sql make query to database
            Statement statement = connection.createStatement();
            //query
            String query = "select user_login from user";

            // variable for saving result of query
            rs = statement.executeQuery(query);
            int chek = 0;

            //check value of login
            while (rs.next()){
                String count = rs.getString(1);
                if(count.equals(login)){
                    chek = 1;
                }
            }

            if(login.equals("rozdis")) {
                CURRENT_ACCAUNT = "rozdis";
                //firstly close current
                Stage stage = (Stage) OpenMainButton.getScene().getWindow();
                //closing current
                stage.close();
                //Now we should open new window(main window for our app
                URL url = new File("src/main/java/FXMLfiles/Admin_panel_main.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
                Parent root1 = FXMLLoader.load(url);
                //create new stage
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Home");
                stage.setScene(new Scene(root1,1200,900));
                stage.show();
            }
            else if (chek == 1){
                //here we will change the window
                outputText.setText("Логин проверен");
                CURRENT_ACCAUNT = login;
                //firstly close current
                Stage stage = (Stage) OpenMainButton.getScene().getWindow();
                //closing current
                stage.close();
                //loginId.setText("sadasdsadasdsadsaasd");
                //Now we should open new window(main window for our app
                URL url = new File("src/main/java/FXMLfiles/Main_sample.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
                Parent root1 = FXMLLoader.load(url);
                //create new stage
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Home");
                stage.setScene(new Scene(root1,1200,900));
                stage.show();


            }
            else {
                //create alert
                Alert a = new Alert(AlertType.ERROR);
                //set info inside of alert
                a.setContentText("Неверный логин, проверте правельность ввода и попробуйте ещё раз. Возможно вы не авторизованы в системе!");
                a.show();
            }

            //closing connection with data base
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    void ClickOnEatenFood() throws IOException{
        Stage stage;
        URL url = new File("src/main/java/FXMLfiles/EatenFood.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();
    }

    @FXML
    void ClickOnProfileButton() throws IOException {
        //firstly close current
        //Stage stage = (Stage) profileButton.getScene().getWindow();
        //closing current
        //stage.close();
        Stage stage;
        //Now we should open new window(main window for our app
        URL url = new File("src/main/java/FXMLfiles/Profile.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();

    }

    @FXML
    void AddProductInMainClick() throws IOException {
        Stage stage;
        URL url = new File("src/main/java/FXMLfiles/AddFood.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();

    }

    @FXML
    void CheckLoginAction(){
        if(LoginOfManager.getText().equals("admin"))
            AddProductButton.setDisable(false);
        else
            AddProductButton.setDisable(true);
    }

    @FXML
    void AddProductClick() {
        Connection connection;

        try {

            String newName =  AddProductName.getText();
            int newCalories =  Integer.parseInt(AddProductCalories.getText());
            float newProteins = Float.parseFloat(AddProductProt.getText());
            float newFats = Float.parseFloat(AddProductFats.getText());
            float newCarbohydrates = Float.parseFloat(AddProductCarb.getText());
            //connection to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();
            //statement helps sql make query to database
            st = connection.createStatement();
            String queryCheck = "select food_name from food";

            // variable for saving result of query
            rs = st.executeQuery(queryCheck);
            int chek = 0;

            //check value of login
            while (rs.next()){
                String str = rs.getString(1);
                if(!(str.equals(newName))){
                    chek = 1;
                }
                else {
                    chek = 10;
                    break;
                }
            }

            if(chek == 1) {

                //query
                String query = "INSERT INTO food(food_name, food_calories, food_proteins, food_fats, food_carbohydrates, food_type_id)  VALUES ('" + newName + "', " + newCalories + ", " + newProteins + ", " + newFats + ", '" + newCarbohydrates + "'," + typeOfFood +");";

                // variable for saving result of query
                st.execute(query);
                connection.close();
                Alert a = new Alert(AlertType.INFORMATION);
                a.setContentText("Продукт был успешно добавлен!");
                a.show();
            }
            else {
                Alert a = new Alert(AlertType.ERROR);
                //set info inside of alert
                a.setContentText("Продукт с таким именем уже сущевствует!");
                a.show();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (Exception b){
            Alert a = new Alert(AlertType.ERROR);
            //set info inside of alert
            a.setContentText("Возможно вы неверно ввели значения, проверьте корректность ввода!");
            a.show();
            b.getMessage();
        }
    }

    @FXML
    void ActionChoose1(){ typeOfFood = 1;}
    @FXML
    void ActionChoose2(){ typeOfFood = 2;}
    @FXML
    void ActionChoose3(){ typeOfFood = 3;}
    @FXML
    void ActionChoose4(){ typeOfFood = 4;}
    @FXML
    void ActionChoose5(){ typeOfFood = 5;}
    @FXML
    void ActionChoose6(){ typeOfFood = 6;}
    @FXML
    void ActionChoose7(){ typeOfFood = 7;}

    @FXML
    void RecomendButtonInMainClick() throws IOException{
        //firstly close current
        Stage stage;
        //Now we should open new window(main window for our app
        URL url = new File("src/main/java/FXMLfiles/Recomandations.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();
    }

    @FXML
    void ClickOnLogOut() throws Exception {
        Stage stage = (Stage) LogOutButton.getScene().getWindow();
        //closing current
        stage.close();
        //Now we should open new window(main window for our app
        URL url = new File("src/main/java/FXMLfiles/sample.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();
    }

    @FXML
    void ClickOnFiltr(){
        tableFood.getItems().clear();
        try {
            //connection to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();

            //statement helps sql make query to database
            st = connection.createStatement();
            st.executeQuery("set names utf8");
            st.executeQuery("set character set utf8");
            //query

            String query = "SELECT * FROM food where food_calories between " + inputFrom.getText() + " and " + inputTo.getText();
            // variable for saving result of query
            rs = st.executeQuery(query);

        }catch (Exception e){
            e.printStackTrace();
        }

        Food food;
        try{
            //getting values from our database(table food)
            while (rs.next()){
                int id = rs.getInt("food_id");
                String food_name =rs.getString("food_name");
                int food_calories = rs.getInt("food_calories");
                float food_proteins = rs.getFloat("food_proteins");
                float food_fats = rs.getFloat("food_fats");
                float food_carbohydrates = rs.getFloat("food_carbohydrates");

                food = new Food(id, food_name, food_calories, food_proteins, food_fats, food_carbohydrates);
                foodData.add(food);
            }
            st.close();
            connection.close();

        }catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
            //create alert
            Alert a = new Alert(AlertType.ERROR);
            //set info inside of alert
                a.setContentText("Вы должны ввести оба параметра! Возможно вы ввели их не корректно, проверьте правельность ввода данных");
            a.show();
        }

        try {
            //initialize values in table of food
            idColum.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
            CaloriesColum.setCellValueFactory(new PropertyValueFactory<>("calories"));
            proteinsColum.setCellValueFactory(new PropertyValueFactory<>("proteins"));
            fatsColum.setCellValueFactory(new PropertyValueFactory<>("fats"));
            carbohydratesColum.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));

            tableFood.setItems(foodData);
        }
        catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void FindNormalCalories() throws SQLException{
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
        st = connection.createStatement();
        //query
        String query = "SELECT * FROM user WHERE user_login = '" + CURRENT_ACCAUNT + "'";
        // variable for saving result of query
        rs = st.executeQuery(query);
        rs.next();
        String targetCurrent;
        double activityCoef;
        int ageCurrent = rs.getInt("user_age");
        float weightCurrent = rs.getFloat("user_weight");
        float heightCurrent = rs.getFloat("user_height");
        String genderCurrent = rs.getString("user_gender");
        int activityCurrentId = rs.getInt("user_activity_id");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       // if(targetCurrentId == 1) targetCurrent = "Снижение веса";
       // else if(targetCurrentId == 2) targetCurrent = "Набор веса";
        //else targetCurrent = "Поддержание веса";

        if(activityCurrentId == 1) activityCoef = 1.37;
        else if(activityCurrentId == 2) activityCoef = 1.55;
        else activityCoef = 1.72;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        double brm, normalCalories;
        if(genderCurrent.equals("Мужской")){
            brm = 88.36 + (13.4 * weightCurrent) + (4.8 * heightCurrent) - (5.7 * ageCurrent);
            normalCalories = brm * activityCoef;
            NormalCaloriesText.setText("" + normalCalories);
        }
        else {
            brm = 447.6 + (9.2 * weightCurrent) + (3.1 * heightCurrent) - (4.3 * ageCurrent);
            normalCalories = brm * activityCoef;
            NormalCaloriesText.setText("" + normalCalories);
        }
    }

    @FXML
    void FindNormalCalories2() throws  Exception{
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
        st = connection.createStatement();
        //query
        String query = "SELECT * FROM user WHERE user_login = '" + CURRENT_ACCAUNT + "'";
        // variable for saving result of query
        rs = st.executeQuery(query);
        rs.next();
        String targetCurrent;
        double activityCoef;
        int ageCurrent = rs.getInt("user_age");
        float weightCurrent = rs.getFloat("user_weight");
        float heightCurrent = rs.getFloat("user_height");
        String genderCurrent = rs.getString("user_gender");
        int activityCurrentId = rs.getInt("user_activity_id");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // if(targetCurrentId == 1) targetCurrent = "Снижение веса";
        // else if(targetCurrentId == 2) targetCurrent = "Набор веса";
        //else targetCurrent = "Поддержание веса";

        if(activityCurrentId == 1) activityCoef = 1.37;
        else if(activityCurrentId == 2) activityCoef = 1.55;
        else activityCoef = 1.72;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        double brm, normalCalories;
        if(genderCurrent.equals("Мужской")){
            brm = (10 * weightCurrent) + (6.25 * heightCurrent) - (5 * ageCurrent) + 5;
            normalCalories = brm * activityCoef;
            NormalCaloriesText2.setText("" + normalCalories);
        }
        else {
            brm = (10 * weightCurrent) + (6.25 * heightCurrent) - (5 * ageCurrent) - 161;
            normalCalories = brm * activityCoef;
            NormalCaloriesText2.setText("" + normalCalories);
        }
    }

    @FXML
    void ShowInfoClick(){
        try {
            hideButton.setDisable(true);
            //connection to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();
            st = connection.createStatement();
            //query
            String query = "SELECT * FROM user WHERE user_login = '" + CURRENT_ACCAUNT + "'";
            // variable for saving result of query
            rs = st.executeQuery(query);
            rs.next();
            String targetCurrent, activityCurrrent;
            String loginCurrent = rs.getString("user_login");
            int ageCurrent = rs.getInt("user_age");
            float weightCurrent = rs.getFloat("user_weight");
            float heightCurrent = rs.getFloat("user_height");
            String genderCurrent = rs.getString("user_gender");
            int targetCurrentId = rs.getInt("target_id");
            int activityCurrentId = rs.getInt("user_activity_id");
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if(targetCurrentId == 1) targetCurrent = "Снижение веса";
            else if(targetCurrentId == 2) targetCurrent = "Набор веса";
            else targetCurrent = "Поддержание веса";

            if(activityCurrentId == 1) activityCurrrent = "Низкая";
            else if(activityCurrentId == 2) activityCurrrent = "Умеренная";
            else activityCurrrent = "Высокая";
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            loginTextProfile.setText(loginCurrent);
            targetTextProfile.setText(targetCurrent);
            activityTextProfile.setText(activityCurrrent);
            ageTextProfile.setText("" + ageCurrent);
            weightTextProfile.setText("" + weightCurrent);
            heightTextProfile.setText("" + heightCurrent);
            genderTextProfile.setText(genderCurrent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void LoginByUsername() throws IOException {
        //firstly close current
        Stage stage = (Stage) EnterButton.getScene().getWindow();
        //closing current
        stage.close();
        //Now we should open new window(main window for our app
        URL url = new File("src/main/java/FXMLfiles/sample.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();
    }

    @FXML
    void OpenRegistration() throws IOException {
        //firstly close current
        Stage stage = (Stage) OpenRegButton.getScene().getWindow();
        //closing current
        stage.close();
        //Now we should open new window(main window for our app
        URL url = new File("src/main/java/FXMLfiles/Registration.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
        Parent root1 = FXMLLoader.load(url);
        //create new stage
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Home");
        stage.setScene(new Scene(root1,1200,900));
        stage.show();

    }

    @FXML
    void LoseWeightTargetAction(){
        target = 1;
    }
    @FXML
    void MoreWeightTargetAction(){
        target = 2;
    }
    @FXML
    void SameWeightTargetAction(){
        target = 3;
    }
    @FXML
    void LowActivityAction(){
        activity = 1;
    }
    @FXML
    void EnoughActivityAction(){
        activity = 2;
    }
    @FXML
    void HighActivityAction(){
        activity = 3;
    }

    @FXML
    void Registration(ActionEvent event) {
        Connection connection;


        try {
            init();
            haarSelected();
            newLogin = inputNewLogin.getText();
            age = Integer.parseInt(inputAge.getText());
            weight = Float.parseFloat(inputWeight.getText());
            height = Float.parseFloat(inputHeight.getText());
            //////gender
            javafx.scene.control.RadioButton selection = (javafx.scene.control.RadioButton) RadioButton.getSelectedToggle();
            gender = selection.getText();
            //connection to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();
            //statement helps sql make query to database
            st = connection.createStatement();
            String queryCheck = "select user_login from user";

            // variable for saving result of query
            rs = st.executeQuery(queryCheck);
            int chek = 0;

            //check value of login
            while (rs.next()){
                String str = rs.getString(1);
                if(!(str.equals(newLogin))){
                    chek = 1;
                }
                else {
                    chek = 10;
                    break;
                }
            }

            if(chek == 1) {
                CURRENT_ACCAUNT = newLogin;
                FileWriter writer = new FileWriter("Reg_names.txt");
                writer.write(CURRENT_ACCAUNT + "\n");
                startCamera(2, newLogin);
                writer.close();
                //query
                String query = "INSERT INTO user(user_login, user_age, user_weight, user_height, user_gender, target_id, user_activity_id)  VALUES ('" + newLogin + "', " + age + ", " + weight + ", " + height + ", '" + gender + "'," + target + ", " + activity + ");";

                // variable for saving result of query
                st.execute(query);
                connection.close();
                Stage stage = (Stage) button.getScene().getWindow();
                //closing current
                stage.close();
                //Now we should open new window(main window for our app
                URL url = new File("src/main/java/FXMLfiles/Main_sample.fxml").toURI().toURL();// url for FXMLLoadder otherwise it doesn`t work
                Parent root1 = FXMLLoader.load(url);
                //create new stage
                stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Home");
                stage.setScene(new Scene(root1, 1200, 900));
                stage.show();
            }
            else {
                Alert a = new Alert(AlertType.ERROR);
                //set info inside of alert
                a.setContentText("Пользователь с таким логином уже сущевствует, введите другой!");
                a.show();
            }
        }catch (SQLException e){
            Alert a = new Alert(AlertType.ERROR);
            //set info inside of alert
            a.setContentText("Возможно вы ввели неправельные значения в поля, проверьте коректность ввода!");
            a.show();
            e.printStackTrace();
        }catch (Exception b){
            b.getMessage();
            Alert a = new Alert(AlertType.ERROR);
            //set info inside of alert
            a.setContentText("Возможно вы ввели неправельные значения в поля, проверьте коректность ввода!");
            a.show();
            b.printStackTrace();
        }
        stopCamera();

    }

    @FXML
    void ClickOnUpdateButton(){
        try{
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();

            //statement helps sql make query to database
            st = connection.createStatement();
            //query
            String query = "UPDATE food SET food_name = '" + outName.getText() + "', food_calories = " + Integer.parseInt(outCal.getText()) + ", food_proteins = " + Float.parseFloat(outProt.getText()) +
                    ", food_fats = " + Float.parseFloat(outFats.getText()) + ", food_carbohydrates = " + Float.parseFloat(outCarb.getText()) +" where food_id = " + idOfCurrentProduct;
            // variable for saving result of query
            st.execute(query);
            initialize();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setContentText("Данные были успешно измененны!");
        }catch (Exception e){
            e.getMessage();
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Вы ввели не все, либо не корректные данные в поля ввода! Проверьте корректность ввода данных!");
            a.show();
        }
    }
    @FXML
    void ClickOnDelete() {
        try{
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();

            //statement helps sql make query to database
            st = connection.createStatement();
            ////////////disable checking foreign key in children table
            st.execute("SET FOREIGN_KEY_CHECKS=0;");
            //query
            String query = "delete from food where food_id = " + idOfCurrentProduct;
            // variable for saving result of query
            st.execute(query);
            initialize();
            Alert a = new Alert(AlertType.INFORMATION);
            a.setContentText("Данные были успешно удалены!");
        }catch (Exception e){
            e.getMessage();
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Невозможно удалить данные!");
            a.show();
        }
    }

    @FXML
    public void initialize(){


        try {
            //connection to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();

            //statement helps sql make query to database
             st = connection.createStatement();
            st.executeQuery("set names utf8");
            st.executeQuery("set character set utf8");
            //query
            String query = "SELECT * FROM food";
            // variable for saving result of query
            rs = st.executeQuery(query);

        }catch (Exception e){
            e.printStackTrace();
        }

        Food food;
        try{
            if(!tableFood.getItems().isEmpty())
                tableFood.getItems().clear();
            //getting values from our database(table food)
            while (rs.next()){
                int id = rs.getInt("food_id");
                String food_name =rs.getString("food_name");
                int food_calories = rs.getInt("food_calories");
                float food_proteins = rs.getFloat("food_proteins");
                float food_fats = rs.getFloat("food_fats");
                float food_carbohydrates = rs.getFloat("food_carbohydrates");

                food = new Food(id, food_name, food_calories, food_proteins, food_fats, food_carbohydrates);
                foodData.add(food);
            }
            st.close();
            connection.close();

        }catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
        }

        try {
            //initialize values in table of food
            idColum.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
            CaloriesColum.setCellValueFactory(new PropertyValueFactory<>("calories"));
            proteinsColum.setCellValueFactory(new PropertyValueFactory<>("proteins"));
            fatsColum.setCellValueFactory(new PropertyValueFactory<>("fats"));
            carbohydratesColum.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));

            tableFood.setItems(foodData);
            TableView.TableViewSelectionModel<Food> selectionModel = tableFood.getSelectionModel();
            selectionModel.selectedItemProperty().addListener(new ChangeListener<Food>() {
                @Override
                public void changed(ObservableValue<? extends Food> observable, Food oldValue, Food newValue) {
                    if(newValue != null){
                        outName.setText(newValue.getName());
                        outCal.setText("" + newValue.getCalories());
                        outProt.setText("" + newValue.getProteins());
                        outFats.setText("" + newValue.getFats());
                        outCarb.setText("" + newValue.getCarbohydrates());
                        idOfCurrentProduct = newValue.getId();
                    }
                }
            });
        }
        catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
        }


    }

    private void  initTable(int conditionToSort){
        tableFood.getItems().clear();
        try {
            //connection to database
            DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
            connection = databaseConnection.getConnection();

            //statement helps sql make query to database
            st = connection.createStatement();
            st.executeQuery("set names utf8");
            st.executeQuery("set character set utf8");
            //query
            String query = "SELECT * FROM food where food_type_id = " + conditionToSort;
            // variable for saving result of query
            rs = st.executeQuery(query);

        }catch (Exception e){
            e.printStackTrace();
        }

        Food food;
        try{
            //getting values from our database(table food)
            while (rs.next()){
                int id = rs.getInt("food_id");
                String food_name =rs.getString("food_name");
                int food_calories = rs.getInt("food_calories");
                float food_proteins = rs.getFloat("food_proteins");
                float food_fats = rs.getFloat("food_fats");
                float food_carbohydrates = rs.getFloat("food_carbohydrates");
                /*System.out.println("Thissadasdadsdads: " + id + ""  + "\n" +
                        "\n" +food_name + "" +
                        "\n" + food_calories + "" +
                        "\n" + food_proteins + "" +
                        "\n" + food_fats + "" +
                        "\n" + food_carbohydrates);*/
                food = new Food(id, food_name, food_calories, food_proteins, food_fats, food_carbohydrates);
                foodData.add(food);
            }
            st.close();
            connection.close();

        }catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
        }

        try {
            //initialize values in table of food
            idColum.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
            CaloriesColum.setCellValueFactory(new PropertyValueFactory<>("calories"));
            proteinsColum.setCellValueFactory(new PropertyValueFactory<>("proteins"));
            fatsColum.setCellValueFactory(new PropertyValueFactory<>("fats"));
            carbohydratesColum.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));

            tableFood.setItems(foodData);
        }
        catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void SortTable1(){
        initTable(1);
    }
    @FXML
    void SortTable2(){
        initTable(2);
    }
    @FXML
    void SortTable3(){
        initTable(3);
    }
    @FXML
    void SortTable4(){
        initTable(4);
    }
    @FXML
    void SortTable5(){
        initTable(5);
    }
    @FXML
    void SortTable6(){
        initTable(6);
    }
    @FXML
    void SortTable7(){
        initTable(7);
    }

     ////////////////////////////////////////////////Controller from faceDetecion because i need to compile my program/////////////////////////////

    public void init()
    {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;

        // set a fixed width for the frame
//        originalFrame.setFitWidth(600);
        // preserve image ratio
//        originalFrame.setPreserveRatio(true);
    }

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    public void startCamera(int chooser, String login)
    {
        if (!this.cameraActive)
        {



            // start the video capture
            this.capture.open(0);

            // is the video stream available?
            if (this.capture.isOpened())
            {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run()
                    {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame(chooser, login);
                        // convert and show the frame
                        // Image imageToShow = Utils.mat2Image(frame);
                        // updateImageView(originalFrame, imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

            }
            else
            {
                // log the error
                System.err.println("Failed to open the camera connection...");
            }
        }
        else
        {
            // the camera is not active at this point
            this.cameraActive = false;
            // stop the timer
            this.stopAcquisition();
        }
    }

    protected void stopCamera(){
        this.cameraActive = false;
        this.stopAcquisition();
        this.stopAcquisition();
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Mat grabFrame(int chooser, String login)
    {
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty())
                {
                    if(chooser == 1) {
                        // face detection
                        this.detectAndDisplay(frame);
                    }
                    else{
                        this.detectAndDisplayWhileRegistration(frame, login);
                    }
                }

            }
            catch (Exception e)
            {
                // log the (full) error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Method for face detection and tracking
     *
     * @param frame
     *            it looks for faces in this frame
     */
    private void detectAndDisplay(Mat frame)
    {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0)
        {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0)
            {
                this.absoluteFaceSize = Math.round(height * 0.3f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 1, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize ), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

        int i = 0;
        for (Rect rect : facesArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0)); // frame is Mat
            Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);

            Mat resizedImg = new Mat();
            Mat image_roi = new Mat(frame,rectCrop);
            Imgproc.resize(image_roi, resizedImg, new Size(125, 150));
            resizedImg.convertTo(resizedImg, CV_8UC3);
            Imgcodecs.imwrite("probes/face"+ i +".png",resizedImg);
            i++;
        }

    }

    private void detectAndDisplayWhileRegistration(Mat frame, String login){
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0)
        {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0)
            {
                this.absoluteFaceSize = Math.round(height * 0.3f);
            }
        }

        // detect faces
        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 1, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize ), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);

        int i = 0;
        for (Rect rect : facesArray) {
            Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0)); // frame is Mat
            Rect rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);

            Mat resizedImg = new Mat();
            Mat image_roi = new Mat(frame,rectCrop);
            Imgproc.resize(image_roi, resizedImg, new Size(125, 150));
            resizedImg.convertTo(resizedImg, CV_8UC3);
            Imgcodecs.imwrite("gallery/"+ login + 2 +".png",resizedImg);
            i++;
        }
    }


    /**
     * The action triggered by selecting the Haar Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    public void haarSelected()
    {
        // check whether the lpb checkbox is selected and deselect it


        this.checkboxSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
    }

    /**
     * The action triggered by selecting the LBP Classifier checkbox. It loads
     * the trained set to be used for frontal face detection.
     */
    @FXML
    protected void lbpSelected(Event event)
    {
        // check whether the haar checkbox is selected and deselect it
        if (this.haarClassifier.isSelected())
            this.haarClassifier.setSelected(false);

        this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
    }

    /**
     * Method for loading a classifier trained set from disk
     *
     * @param classifierPath
     *            the path on disk where a classifier trained set is located
     */
    private void checkboxSelection(String classifierPath)
    {
        // load the classifier(s)
        this.faceCascade.load(classifierPath);

        // now the video capture can start

    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition()
    {
        if (this.timer!=null && !this.timer.isShutdown())
        {
            try
            {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened())
        {
            // release the camera
            this.capture.release();
        }
    }

    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view
     *            the {@link ImageView} to update
     * @param image
     *            the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image)
    {
        Utils.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    public void setClosed()
    {
        this.stopAcquisition();
    }
    /////////////////////////////////////////////////Controller from faceDetecion/////////////////////////////////////////////////////////////////

}
