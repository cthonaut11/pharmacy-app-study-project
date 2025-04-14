package pharmacy_store;

import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.Customer;
import pharmacy_store.entity.Medicine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineService {

    private final Connection connection;

    public MedicineService(Connection connection) {
        this.connection = connection;
    }

    public static List<Medicine> getAllMedicine() throws SQLException {
        String sql = """
                SELECT *
                FROM medicine
                """;
        List<Medicine> medicines = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Medicine medicine = new Medicine();
                medicine.setId(resultSet.getInt("id"));
                medicine.setName(resultSet.getString("name"));
                medicine.setCategory(resultSet.getString("category"));
                medicine.setId_company_producer(resultSet.getInt("id_company_producer"));
                medicine.setQuantity(resultSet.getInt("quantity"));
                medicine.setPurchase_price(resultSet.getDouble("purchase_price"));
                medicine.setSelling_price(resultSet.getDouble("selling_price"));
                medicine.setExpiry_date(resultSet.getDate("expiry_date").toLocalDate());
                medicines.add(medicine);
            }
        }
        return medicines;
    }

    public static List<Customer> getAllCustomers() throws SQLException{
        String sql = """
                SELECT * 
                FROM customer;
                """;

        List<Customer> customers = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setFirst_name(resultSet.getString("first_name"));
                customer.setLast_name(resultSet.getString("last_name"));
                customer.setPhoneNumber(resultSet.getString("phone_number"));
                customer.setDiscount(resultSet.getInt("discount"));
                customers.add(customer);
            }
        }
        return customers;
    }
}
