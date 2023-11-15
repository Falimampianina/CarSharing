package carsharing;

import java.util.List;

public interface CarDAO {
    void createTable();
    List<Car> findAll();

    List<Car> findByCompanyId(int companyId);

    void createAndSaveCar(String name , int CompanyId);

    void dropConstraint();

    List<Car> findByID(int car_id);

    void updateAvailable(String b , int car_id);

    List<Car> findAvailableCar(int company_id);
}
