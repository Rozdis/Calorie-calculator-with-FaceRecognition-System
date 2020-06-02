import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/CalculatorOfCalories?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "12345";

    private DatabaseConnection() throws SQLException{
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASS);
        } catch ( Exception b){
            System.out.println("Database Connection Creation Failed : " + b.getMessage());
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException{
        if(instance == null){
            instance = new DatabaseConnection();
        } else if(instance.getConnection().isClosed()){
            instance = new DatabaseConnection();
        }

        return instance;
    }
}

