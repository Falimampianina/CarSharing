package carsharing;

import carsharing.db.CustomerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController implements CustomerDAO {

    private final String ROOT = "jdbc:h2:./src/carsharing/db/";
    private final String dbName;

    public CustomerController(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement()
        ){
            String SELECT = "SELECT * FROM CUSTOMER;";
            ResultSet resultSet = stmt.executeQuery(SELECT);
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Customer customer = new Customer(name);
                customer.setId(i);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTable() {
        try (
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement()
        ){
            //stmt.execute("DROP TABLE IF EXISTS CUSTOMER;");
            String CREATE = "CREATE TABLE IF NOT EXISTS CUSTOMER (ID int not null PRIMARY KEY AUTO_INCREMENT," +
                    "NAME varchar not null UNIQUE," +
                    "RENTED_CAR_ID int," +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR (ID));";
            stmt.execute(CREATE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findById(int customer_id) {
        List<Customer> customers = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement()
        ){
            String SELECT_ID = "SELECT * FROM CUSTOMER WHERE ID = %d;";
            ResultSet resultSet = stmt.executeQuery(String.format(SELECT_ID,customer_id));
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int car_rented_id = resultSet.getInt("RENTED_CAR_ID");
                Customer customer = new Customer(name);
                customer.setId(i);
                customer.setRented_car_id(car_rented_id);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRentedCarId(int rented_car_id, int customer_id) {
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement()
        ){
            String UPDATE = "UPDATE CUSTOMER SET RENTED_CAR_ID = %d WHERE ID = %d";
            stmt.executeUpdate(String.format(UPDATE,rented_car_id,customer_id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateRentedCarId(int customer_id) {
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement()
        ){
            String UPDATE = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = %d";
            stmt.executeUpdate(String.format(UPDATE,customer_id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAndSaveCustomer(String name) {
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement()
        ){
            con.setAutoCommit(true);
            String INSERT = "INSERT INTO CUSTOMER (NAME) VALUES ('%s');";
            stmt.executeUpdate(String.format(INSERT, name));
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}