package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyController implements CompanyDAO{
    private final String ROOT = "jdbc:h2:./src/carsharing/db/";
    private final String dbName;

    private final String DROP = "DROP TABLE IF EXISTS COMPANY;";

    private final String CREATE ="CREATE TABLE IF NOT EXISTS COMPANY (ID int not null PRIMARY KEY AUTO_INCREMENT," +
                                                                      "NAME varchar not null UNIQUE);";
    private final String INSERT ="INSERT INTO COMPANY (NAME) VALUES ('%s');";


    public CompanyController(String dbName) {
        this.dbName = dbName;
    }

    public void createTable(){
        try (
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
                ){
            //stmt.execute(DROP);
            stmt.execute(CREATE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Company> findById(int company_id) {
        List<Company> companies = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
        ){
            String SELECT = "SELECT * FROM COMPANY WHERE ID = %d;";
            ResultSet resultSet = stmt.executeQuery(String.format(SELECT,company_id));
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Company company = new Company(name);
                company.setId(i);
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Insert a new company on the database
    @Override
    public void createAndSaveCompany(String name) throws SQLException {
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
                ){
            con.setAutoCommit(true);
            stmt.execute(String.format(INSERT, name));
            System.out.println("The company was created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //find all companies on the database
    @Override
    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();
        try(
                Connection con = DriverManager.getConnection(ROOT+this.dbName);
                Statement stmt = con.createStatement();
                ){
            String SELECT = "SELECT * FROM COMPANY;";
            ResultSet resultSet = stmt.executeQuery(SELECT);
            while(resultSet.next()){
                int i = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                Company company = new Company(name);
                company.setId(i);
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
