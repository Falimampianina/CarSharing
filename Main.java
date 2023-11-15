package carsharing;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // write your code here
        //String dbName = args[1];
        String dbName = "carsharing";
        User user = new User(new CompanyController(dbName), new CarController(dbName), new CustomerController(dbName));
        user.createTables();
        user.homeMenu();

    }
}