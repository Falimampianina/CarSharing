package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarController implements CarDAO{

    private final String ROOT = "jdbc:h2:./src/carsharing/db/";
    private final String dbName;

    private final String DROP = "DROP TABLE IF EXISTS CAR;";


    private final String SELECT = "SELECT * FROM CAR;";

    private final String SELECT_ID = "SELECT * FROM CAR WHERE ID = %d;";

    public CarController(String dbName) {
        this.dbName = dbName;
    }

    public void dropConstraint(){
        try (
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            String DROP_CONSTRAINT = "ALTER TABLE CAR DROP CONSTRAINT IF EXISTS Company_Car;";
            stmt.execute(DROP_CONSTRAINT);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findByID(int car_id) {
        List<Car> cars = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            ResultSet resultSet = stmt.executeQuery(String.format(SELECT_ID,car_id));
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyId = resultSet.getInt("COMPANY_ID");
                Car car = new Car(name, companyId);
                car.setID(i);
                cars.add(car);
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAvailable(String b , int car_id) {
        try (
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            //stmt.execute(DROP);
            String UPDATE = "UPDATE CAR SET AVAILABLE = %s WHERE ID = %d;";
            stmt.executeUpdate(String.format(UPDATE,b,car_id));
            //String ADD_CONSTRAINT = "ALTER TABLE CAR ADD CONSTRAINT Company_CAr FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID) ON DELETE CASCADE;";
            //stmt.execute(ADD_CONSTRAINT);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findAvailableCar(int company_id) {
        List<Car> cars = findAll();
        for (Iterator<Car> it = cars.iterator(); it.hasNext();){
            Car car = it.next();
            if(!car.isAvailable()){
                it.remove();
            }
        }
        return cars;
    }

    @Override
    public void createTable() {
        try (
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            //stmt.execute(DROP);
            String CREATE = "CREATE TABLE IF NOT EXISTS CAR (ID int not null PRIMARY KEY AUTO_INCREMENT," +
                    "NAME varchar not null UNIQUE," +
                    "COMPANY_ID int not null," +
                    "AVAILABLE BOOLEAN DEFAULT TRUE," +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY (ID));";
            stmt.executeUpdate(CREATE);
            //String ADD_CONSTRAINT = "ALTER TABLE CAR ADD CONSTRAINT Company_CAr FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID) ON DELETE CASCADE;";
            //stmt.execute(ADD_CONSTRAINT);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            ResultSet resultSet = stmt.executeQuery(SELECT);
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                int companyId = resultSet.getInt("COMPANY_ID");
                boolean available = resultSet.getBoolean("AVAILABLE");
                Car car = new Car(name, companyId);
                car.setID(i);
                car.setAvailable(available);
                cars.add(car);

            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> findByCompanyId(int companyId) {
        List<Car> cars = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            String SELECT_BY_COMPANY_ID = "SELECT * FROM CAR WHERE COMPANY_ID = %d;";
            ResultSet resultSet = stmt.executeQuery(String.format(SELECT_BY_COMPANY_ID,companyId));
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                boolean available = resultSet.getBoolean("AVAILABLE");
                Car car = new Car(name, companyId);
                car.setID(i);
                car.setAvailable(available);
                cars.add(car);
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAndSaveCar(String name, int CompanyId) {
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            String INSERT = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('%s', %d);";
            stmt.execute(String.format(INSERT, name , CompanyId));
            System.out.println("The car was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
