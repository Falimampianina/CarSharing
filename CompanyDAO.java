package carsharing;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDAO {
    void createAndSaveCompany(String name) throws SQLException;

    List<Company> findAll();

    void createTable();

    List<Company> findById(int company_id);
}
