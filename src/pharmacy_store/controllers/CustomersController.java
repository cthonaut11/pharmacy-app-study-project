package pharmacy_store.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pharmacy_store.dao.CustomerDao;
import pharmacy_store.entity.Customer;
import pharmacy_store.exception.DaoException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomersController {

    private ObservableList<Customer> customersData = FXCollections.observableArrayList();

    private Stage stage;
    private Scene scene;
    public AnchorPane root;


    @FXML private Button storageButton;

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
            List<Customer> customers = CustomerDao.getInstance().getAllCustomers();

            customersData.clear();
            customersData.addAll(customers);

            customerTable.setItems(customersData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void deleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            showAlert("Ошибка", "Выберите клиента для удаления", Alert.AlertType.WARNING);
            return;
        }

        // Диалог подтверждения
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Удаление клиента: " + selectedCustomer.getFirst_name() + " " + selectedCustomer.getLast_name());
        confirmation.setContentText("Вы уверены? Это действие нельзя отменить. В связанных полях будет значение NULL");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = CustomerDao.getInstance().delete(selectedCustomer.getId());
                if (isDeleted) {
                    customersData.remove(selectedCustomer);
                    showAlert("Успех", "Клиент успешно удален", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Ошибка", "Не удалось удалить клиента", Alert.AlertType.ERROR);
                }
            } catch (DaoException e) {
                handleDeletionError(e, selectedCustomer);
            }
        }
    }

    private void handleDeletionError(DaoException e, Customer customer) {
        String errorMessage;
        if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
            errorMessage = "Невозможно удалить клиента - есть связанные продажи";
        } else {
            errorMessage = "Ошибка базы данных: " + e.getMessage();
        }
        showAlert("Ошибка", errorMessage, Alert.AlertType.ERROR);
    }

    @FXML
    private void addNewCustomer() {
        // Создаем диалоговое окно
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Добавление нового клиента");
        dialog.setHeaderText("Введите данные клиента");

        // Устанавливаем кнопки
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Создаем форму для ввода данных
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Имя");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Фамилия");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Телефон");
        TextField discountField = new TextField();
        discountField.setPromptText("Скидка (%)");

        grid.add(new Label("Имя:"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Фамилия:"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Телефон:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Скидка (%):"), 0, 3);
        grid.add(discountField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Преобразуем результат в объект Customer при нажатии Save
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Customer customer = new Customer();
                customer.setFirst_name(firstNameField.getText());
                customer.setLast_name(lastNameField.getText());
                customer.setPhoneNumber(phoneField.getText());

                try {
                    customer.setDiscount(Integer.parseInt(discountField.getText()));
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Введите корректное число для скидки", Alert.AlertType.ERROR);
                    return null;
                }

                return customer;
            }
            return null;
        });

        Optional<Customer> result = dialog.showAndWait();

        result.ifPresent(customer -> {
            try {
                // Сохраняем клиента через DAO
                Customer savedCustomer = CustomerDao.getInstance().save(customer);
                customersData.add(savedCustomer);
                customerTable.refresh();
                showAlert("Успех", "Клиент успешно добавлен", Alert.AlertType.INFORMATION);
            } catch (DaoException e) {
                showAlert("Ошибка", "Не удалось сохранить клиента: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
