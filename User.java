package carsharing;

import carsharing.db.CustomerDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    private final Scanner scanner = new Scanner(System.in);

    private final CompanyDAO companyController;
    private final CarDAO carDAO;

    private final CustomerDAO customerDAO;

    private Company activCompany;
    private Customer activCustomer;

    public User(CompanyDAO companyController, CarDAO carDAO, CustomerDAO customerDAO) {

        this.companyController = companyController;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
    }

    //Page 0
    public void homeMenu() throws SQLException {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
        try{
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1){
                ManagerMenu();
            }else if(choice ==0){
                System.exit(0);
            }else if(choice == 2){
                showCustomerList();
            }else if(choice == 3){
                createCustomer();
            } else{
                System.out.println("Unknown command");
                homeMenu();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Unknown command");
            homeMenu();
        }
    }

    private void showCustomerList() throws SQLException {
        List<Customer> customers = customerDAO.findAll();
        if(!customers.isEmpty()){
            int i = 1;
            for(Customer customer : customers){
                System.out.println(i +". "+ customer.getName());
                i++;
            }
            System.out.println("0. Back");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice<=customers.size() && choice > 0){
                activCustomer = customers.get(choice-1);
                carMenu();
            }else if(choice == 0){
                homeMenu();
            }else{
                System.out.println("Unknown command");
                homeMenu();
            }
        }else{
            System.out.println("The customer list is empty!");
            homeMenu();
        }
    }

    private void carMenu() throws SQLException {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 0){
            homeMenu();
        }else if(choice == 2){
            returnACar();
        }else if (choice == 3){
            showRentedCar();
        }else if(choice == 1){
            rentACar();
        }else{
            System.out.println("Unknown command");
            homeMenu();
        }
    }

    private void rentACar() throws SQLException {
        List<Car> rentedCar = carDAO.findByID(activCustomer.getRented_car_id());
        if(rentedCar.isEmpty()){
            //Customer have to choose a company in which he would like to rent a car
            chooseCompany();
        }else{
            System.out.println("You've already rented a car!");
            carMenu();
        }
    }

    private void chooseCompany() throws SQLException {
        List<Company> companies = companyController.findAll();
        if(!companies.isEmpty()){
            System.out.println("Choose a company:");
            int i = 1;
            for(Company company : companies){
                System.out.println(i +". " + company.getName());
                i++;
            }
            System.out.println("0. Back");
            int choice = Integer.parseInt(scanner.nextLine());
            if((choice > 0 && choice <= companies.size())){
                activCompany = companies.get(choice - 1);
                chooseACar();
            }else if (choice == 0){
                carMenu();
            }else{
                System.out.println("Unknown command");
                carMenu();
            }
        }else{
            System.out.println("The company list is empty!");
            homeMenu();
        }
    }

    private void chooseACar() throws SQLException {
        List<Car> availableCars = carDAO.findAvailableCar(activCompany.getId());
        if(availableCars.isEmpty()){
            System.out.println("No available cars in the "+ activCompany.getName() + " company");
            chooseCompany();
        }else{
            int i = 1;
            for (Car car : availableCars){
                System.out.println(i + ". " + car.getNAME());
                i++;
            }
            System.out.println("0. Back");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= availableCars.size()){
                System.out.println("You rented '" + availableCars.get(choice-1).getNAME()+ "'");
                carDAO.updateAvailable("FALSE", availableCars.get(choice-1).getID());
                customerDAO.updateRentedCarId(availableCars.get(choice-1).getID(), activCustomer.getId());
                List<Customer> customer = customerDAO.findById(activCustomer.getId());
                activCustomer = customer.get(0);
                carMenu();
            }
            carMenu();
        }
    }

    private void showRentedCar() throws SQLException {
        List<Car> rentedCar = carDAO.findByID(activCustomer.getRented_car_id());
        if(rentedCar.isEmpty()){
            System.out.println("You didn't rent a car!");
            carMenu();
        }else{
            System.out.println("Your rented car:");
            System.out.println(rentedCar.get(0).getNAME());
            System.out.println("Company:");
            List<Company> company = companyController.findById(rentedCar.get(0).getCOMPANY_ID());
            System.out.println(company.get(0).getName());
            carMenu();
        }
    }

    private void returnACar() throws SQLException {
        List<Car> rentedCar = carDAO.findByID(activCustomer.getRented_car_id());
        if(rentedCar.isEmpty()){
            System.out.println("You didn't rent a car!");
            carMenu();
        }else{
            carDAO.updateAvailable("TRUE", activCustomer.getRented_car_id());
            customerDAO.updateRentedCarId(activCustomer.getId());
            System.out.println("You've returned a rented car!");
            carMenu();
        }
    }

    private void createCustomer() throws SQLException {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDAO.createAndSaveCustomer(name);
        homeMenu();
    }

    //Page 1
    public void ManagerMenu() throws SQLException {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        int choice = scanner.nextInt();
            if(choice == 0){
                homeMenu();
            }else if(choice == 1){
                List<Company> companies = companyController.findAll();
                if(!companies.isEmpty()){
                    System.out.println("Choose a company: ");
                    for (Company company : companies){
                        System.out.println(company.getId() + ". " + company.getName());
                    }
                    System.out.println("0. Back");
                    System.out.println();
                    selectCompanyManager();
                }else{
                    System.out.println("The company list is empty!");
                    ManagerMenu();
                }

            }else if(choice == 2){
                System.out.println("Enter the company name: ");
                scanner.nextLine();
                String name = scanner.nextLine();
                companyController.createAndSaveCompany(name);
                ManagerMenu();
            }else {
                System.out.println("Unknown command");
                ManagerMenu();
            }
    }

    //Allows the manager to select one company or to go to the page 1
    private void selectCompanyManager() throws SQLException {
        List<Company> companies = companyController.findAll();
        List<Integer> companyIds = new ArrayList<>();
        for (Company company : companies){
            companyIds.add(company.getId());
        }
        scanner.nextLine();
        int choice = Integer.parseInt(scanner.nextLine());
        if (companyIds.contains(choice)){
            System.out.println(companies.get(choice-1).getName()+ " company");
            activCompany = companies.get(choice-1);
            viewOrAddCar();
        }else if(choice == 0){
            ManagerMenu();
        }else{
            System.out.println("Unknown command");
            ManagerMenu();
        }
    }

    //Allows to print the cars from a company or to add a car to it
    private void viewOrAddCar() throws SQLException {
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1){
            List<Car> cars = carDAO.findByCompanyId(activCompany.getId());
            if (!cars.isEmpty()) {
                System.out.println("Car list:");
                int i = 1;
                for (Car car : cars) {
                    System.out.println(i+". "+car.getNAME());
                    i++;
                }
            }else{
                System.out.println("The car list is empty!");
                //subMenu();
            }
            //System.out.println();
            viewOrAddCar();
        }else if(choice == 2){
            System.out.println("Enter the car name: ");
            String name = scanner.nextLine();
            carDAO.createAndSaveCar(name, activCompany.getId());
            viewOrAddCar();
        }else if(choice == 0){
            ManagerMenu();
        }else{
            System.out.println("Unknown command!");
            ManagerMenu();
        }
    }

    /*private void fifthMenu() throws SQLException {
        viewOrAddCar();
    }*/

    //Must run before adding and saving company on the database
    public void createTables(){
        //carDAO.dropConstraint();
        companyController.createTable();
        carDAO.createTable();
        customerDAO.createTable();
    }
}