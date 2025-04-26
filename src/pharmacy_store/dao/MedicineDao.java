package pharmacy_store.dao;

import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.Medicine;
import pharmacy_store.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDao {

    public static final MedicineDao INSTANCE = new MedicineDao();
    private static final String DELETE_SQL= """
            DELETE FROM medicine
            WHERE id = ?
            """;

    private static final String FIND_BY_ID_SQL= """
            Select id, name, category, id_company_producer, quantity, purchase_price, selling_price, expiry_date
            from medicine
            where id = ?
            """;

    private static final String SAVE_SQL= """
            INSERT INTO medicine (name, category, id_company_producer, quantity, purchase_price, selling_price, expiry_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE medicine 
            SET quantity = ?
            where id = ?
            """;

    public List<Medicine> getAllMedicine() throws SQLException {
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



    private MedicineDao() {
    }

    public Medicine update(Medicine medicine){
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)){
            preparedStatement.setInt(1, medicine.getQuantity());
            preparedStatement.setInt(2, medicine.getId());

            preparedStatement.executeUpdate();

            return medicine;

        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    public Medicine save(Medicine medicine){
        try (Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, medicine.getName());
            preparedStatement.setString(2, medicine.getCategory());
            preparedStatement.setInt(3, medicine.getId_company_producer());
            preparedStatement.setInt(4, medicine.getQuantity());
            preparedStatement.setDouble(5, medicine.getPurchase_price());
            preparedStatement.setDouble(6, medicine.getSelling_price());
            preparedStatement.setDate(7, Date.valueOf(medicine.getExpiry_date()));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                medicine.setId(generatedKeys.getInt("id"));
            }
            return medicine;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(int medicineId) {
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){
            preparedStatement.setInt(1, medicineId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Medicine findById(int medicineId) {
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)){
            preparedStatement.setInt(1, medicineId);

            ResultSet resultSet = preparedStatement.executeQuery();
            Medicine medicine = null;
            if(resultSet.next()){
                medicine = new Medicine();
                medicine.setId(resultSet.getInt("id"));
                medicine.setName(resultSet.getString("name"));
                medicine.setCategory(resultSet.getString("category"));
                medicine.setId_company_producer(resultSet.getInt("id_company_producer"));
                medicine.setQuantity(resultSet.getInt("quantity"));
                medicine.setPurchase_price(resultSet.getDouble("purchase_price"));
                medicine.setSelling_price(resultSet.getDouble("selling_price"));
                medicine.setExpiry_date(resultSet.getDate("expiry_date").toLocalDate());
            }

            return medicine;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MedicineDao getInstance() {
        return INSTANCE;
    }

}
