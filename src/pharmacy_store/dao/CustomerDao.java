package pharmacy_store.dao;

import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.Customer;
import pharmacy_store.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    public static final CustomerDao INSTANCE = new CustomerDao();
    private static final String DELETE_SQL= """
            DELETE FROM customer 
            WHERE id = ?
            """;
    private static final String SAVE_SQL= """
            INSERT INTO customer (first_name, last_name, phone_number, discount)
            VALUES (?, ?, ?, ?)
            """;
    private CustomerDao() {
    }

    public Customer save(Customer customer){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, customer.getFirst_name());
            preparedStatement.setString(2, customer.getLast_name());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setInt(4, customer.getDiscount());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                customer.setId(generatedKeys.getInt("id"));
            }
            return customer;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(int customerId) {
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){
            preparedStatement.setInt(1, customerId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Customer> getAllCustomers() throws SQLException{
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

    public static CustomerDao getInstance() {
        return INSTANCE;
    }

}
