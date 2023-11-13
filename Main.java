package carsharing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try {
            Class.forName("org.h2.Driver");
            Connection con = DriverManager.getConnection("jdbc:h2:D:\\Wintersemester_23_24\\Car Sharing\\Car Sharing\\task\\src\\carsharing\\db\\carsharing");
            System.out.println("connected");
            Statement stmt = con.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS COMPANY( ID integer , Name varchar)";
            stmt.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}