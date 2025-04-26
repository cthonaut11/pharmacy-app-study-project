package pharmacy_store.dao;

import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.Sale;
import pharmacy_store.exception.DaoException;

import java.sql.*;

public class SaleDao {

    public static final SaleDao INSTANCE = new SaleDao();
    private static final String DELETE_SQL= """
            DELETE FROM sale
            WHERE id = ?
            """;
    private static final String SAVE_SQL= """
            INSERT INTO sale (id_customer, total_cost, date)
            VALUES (?, ?, ?)
            """;
    private SaleDao() {
    }

    public Sale save(Sale sale){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)){
            //preparedStatement.setInt(1, sale.getId());

            if(sale.getId_customer() != null && sale.getId_customer() > 0){
                preparedStatement.setInt(1, sale.getId_customer());
            }else{
                preparedStatement.setNull(1, Types.INTEGER);
            }
            preparedStatement.setInt(1, sale.getId_customer());
            preparedStatement.setDouble(2, sale.getTotal_cost());
            preparedStatement.setDate(3, Date.valueOf(sale.getDate()));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                sale.setId(generatedKeys.getInt("id"));
            }
            return sale;
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

    public static SaleDao getInstance() {
        return INSTANCE;
    }
}
