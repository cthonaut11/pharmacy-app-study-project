package pharmacy_store.dao;

import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.Medicine;
import pharmacy_store.exception.DaoException;

import java.sql.*;

public class MedicineDao {

    public static final MedicineDao INSTANCE = new MedicineDao();
    private static final String DELETE_SQL= """
            DELETE FROM medicine
            WHERE id = ?
            """;
    private static final String SAVE_SQL= """
            INSERT INTO medicine (name, category, id_company_producer, quantity, purchase_price, selling_price, expiry_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private MedicineDao() {
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

    public static MedicineDao getInstance() {
        return INSTANCE;
    }

}
