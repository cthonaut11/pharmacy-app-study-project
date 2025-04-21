package pharmacy_store.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pharmacy_store.MedicineService;
import pharmacy_store.db.ConnectionManager;
import pharmacy_store.entity.Medicine;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MainController {

    private ObservableList<Medicine> medicinesData = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    public AnchorPane root;


//    @FXML private Button button;
    @FXML private Button storageButton;

    public void switchToSceneMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML private TableView<Medicine> medicineTable;
    @FXML private TableColumn<Medicine, Integer> idColumn;
    @FXML private TableColumn<Medicine, String> nameColumn;
    @FXML private TableColumn<Medicine, String> categoryColumn;
    @FXML private TableColumn<Medicine, Integer> idCompanyProducerColumn;
    @FXML private TableColumn<Medicine, Integer> quantityColumn;
    @FXML private TableColumn<Medicine, Double> purchasePriceColumn;
    @FXML private TableColumn<Medicine, Double> sellPriceColumn;
    @FXML private TableColumn<Medicine, LocalDate> expiryDate;

    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        idCompanyProducerColumn.setCellValueFactory(new PropertyValueFactory<>("id_company_producer"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchase_price"));
        sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        expiryDate.setCellValueFactory(new PropertyValueFactory<>("expiry_date"));

        loadMedicines();
    }

    private void loadMedicines() {
        try{
            MedicineService medicineService = new MedicineService(ConnectionManager.getConnection());
            List<Medicine> medicines = medicineService.getAllMedicine();

            medicinesData.clear();
            medicinesData.addAll(medicines);

            medicineTable.setItems(medicinesData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
