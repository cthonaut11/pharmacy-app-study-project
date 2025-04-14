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
import pharmacy_store.entity.Customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomersController {

    private ObservableList<Customer> customersData = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    public AnchorPane root;


    @FXML private Button storageButton;

//    @FXML
//    public void switchToSceneStorage(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("Storage.fxml"));
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    public void switchToSceneMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> idColumn;
    @FXML private TableColumn<Customer, String> first_nameColumn;
    @FXML private TableColumn<Customer, String> last_nameColumn;
    @FXML private TableColumn<Customer, String> phone_numberColumn;
    @FXML private TableColumn<Customer, Integer> discountColumn;

    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        first_nameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        last_nameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        phone_numberColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        loadCustomers();
    }

    private void loadCustomers() {
        try{
            MedicineService customerService = new MedicineService(ConnectionManager.getConnection());
            List<Customer> customers = customerService.getAllCustomers();

            customersData.clear();
            customersData.addAll(customers);

            customerTable.setItems(customersData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
