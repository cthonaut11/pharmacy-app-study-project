package pharmacy_store.dao;

import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.SaleItem;
import pharmacy_store.exception.DaoException;

import java.sql.*;

public class SaleItemDao {

    public static final SaleItemDao INSTANCE = new SaleItemDao();
    private static final String DELETE_SQL= """
            DELETE FROM sales_item
            WHERE id = ?
            """;
    private static final String SAVE_SQL= """
            INSERT INTO sales_item (id_sale, id_medicine, quantity, price)
            VALUES (?, ?, ?, ?)
            """;
    private SaleItemDao() {
    }

    public SaleItem save(SaleItem saleItem){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, saleItem.getId());
            preparedStatement.setInt(2, saleItem.getId_sale());
            preparedStatement.setInt(3, saleItem.getId_medicine());
            preparedStatement.setInt(4, saleItem.getQuantity());
            preparedStatement.setDouble(5, saleItem.getPrice());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                saleItem.setId(generatedKeys.getInt("id"));
            }
            return saleItem;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(int saleId) {
        try(Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){
            preparedStatement.setInt(1, saleId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static SaleItemDao getInstance() {
        return INSTANCE;
    }
}
