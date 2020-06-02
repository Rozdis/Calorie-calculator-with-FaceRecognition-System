import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import faceDetection.Utils;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ControllerEatenFood {

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
    @FXML
    private TextField inpuntNumberOfGrams;
    @FXML
    private TableColumn<Food, Integer> gramsColum;
    @FXML
    private TableColumn<Food, String> timeColum;
    @FXML
    private Button profileButton, LogOutButton;
    @FXML
    private Button ButtonAddFoodToTable;

    @FXML
    private ChoiceBox<String> AllFoodList;
    private static Statement st;
    private static ResultSet rs;
    private ObservableList<String> foodData = FXCollections.observableArrayList();
    private ObservableList<Food> eatenFoodData = FXCollections.observableArrayList();



    @FXML
    void ClickOnAddFoodToTable() throws Exception {

        Connection connection;
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
        st = connection.createStatement();
        ///////////////////////////////////////getting id of user
        String queryIdUser = "select user_id from user where user_login = '" + Main.CURRENT_ACCAUNT +"'";
        rs = st.executeQuery(queryIdUser);
        rs.next();
        int userIdCurrent = rs.getInt("user_id");
        String nameOfCurrentFood = AllFoodList.getValue();
        //////////////////////////////////////getting id of food
        String queryIdFood = "select food_id from food where food_name = '" + nameOfCurrentFood +"'";
        rs = st.executeQuery(queryIdFood);
        rs.next();
        int foodIdCurrent = rs.getInt("food_id");
        ////////////////////////////////////////////////////adding our values to the table, named eaten_food
        String query = " insert into eaten_food(food_id, user_id, eaten_food_grams) values(" + foodIdCurrent + ", " +  userIdCurrent +", " + Integer.parseInt(inpuntNumberOfGrams.getText()) + ")";
        st.execute(query);
        initialize();

    }

    @FXML
    void initialize() throws Exception {

        Connection connection;
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        connection = databaseConnection.getConnection();
        //statement helps sql make query to database
        st = connection.createStatement();
        String queryCheck = "select food_name from food";

        rs = st.executeQuery(queryCheck);
        while (rs.next()){
            String str = rs.getString("food_name");
            foodData.add(str);
        }
        AllFoodList.setItems(foodData);
        String queryIdUser = "select user_id from user where user_login = '" + Main.CURRENT_ACCAUNT +"'";
        rs = st.executeQuery(queryIdUser);
        rs.next();
        int userIdCurrent = rs.getInt("user_id");
        Food food;
        //query
        String query = "select eaten_food.food_id, food.food_name, food.food_calories, food.food_proteins, food.food_fats, food.food_carbohydrates, eaten_food.eaten_food_grams," +
                " eaten_food.eaten_food_time from eaten_food inner join food ON eaten_food.food_id=food.food_id where eaten_food.user_id = " + userIdCurrent;
        // variable for saving result of query
        rs = st.executeQuery(query);
        try{
            tableFood.getItems().clear();
            //getting values from our database(table food)
            while (rs.next()){
                int id = rs.getInt("food_id");
                String food_name =rs.getString("food_name");
                int food_calories = rs.getInt("food_calories");
                float food_proteins = rs.getFloat("food_proteins");
                float food_fats = rs.getFloat("food_fats");
                float food_carbohydrates = rs.getFloat("food_carbohydrates");
                int food_grams = rs.getInt("eaten_food_grams");
                String food_time = rs.getTimestamp("eaten_food_time").toString();
                food = new Food(id, food_name, food_calories, food_proteins, food_fats, food_carbohydrates, food_grams, food_time);
                eatenFoodData.add(food);
            }
            try {
                //initialize values in table of food
                idColum.setCellValueFactory(new PropertyValueFactory<>("id"));
                nameColum.setCellValueFactory(new PropertyValueFactory<>("name"));
                CaloriesColum.setCellValueFactory(new PropertyValueFactory<>("calories"));
                proteinsColum.setCellValueFactory(new PropertyValueFactory<>("proteins"));
                fatsColum.setCellValueFactory(new PropertyValueFactory<>("fats"));
                carbohydratesColum.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));
                gramsColum.setCellValueFactory(new PropertyValueFactory<>("grams"));
                timeColum.setCellValueFactory(new PropertyValueFactory<>("time"));

                tableFood.setItems(eatenFoodData);
            }
            catch (Exception e){
                System.out.println("There is exception");
                System.out.println(e.getMessage());
            }
            st.close();
            connection.close();

        }catch (Exception e){
            System.out.println("There is exception");
            System.out.println(e.getMessage());
        }





        connection.close();
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

    public void ClickOnProfileButton(javafx.event.ActionEvent event) throws IOException {
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
}
