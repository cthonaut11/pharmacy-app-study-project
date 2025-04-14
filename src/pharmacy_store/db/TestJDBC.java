package pharmacy_store.db;
import pharmacy_store.entity.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestJDBC {

    public static void main(String[] args) throws SQLException {
        List<Medicine> medicines = getAllMedicine();
        for (Medicine medicine : medicines) {
            System.out.println(medicine.getId() + " " + medicine.getName() + " " + medicine.getExpiry_date());
        }
    }

    private static List<Medicine> getAllMedicine() throws SQLException {
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
}
