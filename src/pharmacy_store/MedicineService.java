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


}
