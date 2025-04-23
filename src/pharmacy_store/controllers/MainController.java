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
import pharmacy_store.dao.MedicineDao;
import pharmacy_store.entity.Medicine;
import pharmacy_store.exception.DaoException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MainController {

    private ObservableList<Medicine> medicinesData = FXCollections.observableArrayList();

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
            List<Medicine> medicines = MedicineDao.getInstance().getAllMedicine();

            medicinesData.clear();
            medicinesData.addAll(medicines);

            medicineTable.setItems(medicinesData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void deleteMedicine() {
        Medicine selectedMedicine = medicineTable.getSelectionModel().getSelectedItem();

        if (selectedMedicine == null) {
            showAlert("Ошибка", "Выберите товар для удаления", Alert.AlertType.WARNING);
            return;
        }

        // Диалог подтверждения
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Подтверждение удаления");
        confirmation.setHeaderText("Удаление товара: " + selectedMedicine.getName());
        confirmation.setContentText("Вы уверены? Это действие нельзя отменить. В связанных полях будет значение NULL");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = MedicineDao.getInstance().delete(selectedMedicine.getId());
                if (isDeleted) {
                    medicinesData.remove(selectedMedicine);
                    showAlert("Успех", "Товар успешно удален", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Ошибка", "Не удалось удалить товар", Alert.AlertType.ERROR);
                }
            } catch (DaoException e) {
                handleDeletionError(e, selectedMedicine);
            }
        }
    }

    @FXML
    private void addNewMedicine() {
        // Создаем диалоговое окно
        Dialog<Medicine> dialog = new Dialog<>();
        dialog.setTitle("Добавление нового товара");
        dialog.setHeaderText("Введите данные товара");

        // Устанавливаем кнопки
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Создаем форму для ввода данных
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Наименование");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Категория");
        TextField idCompanyProducerField = new TextField();
        idCompanyProducerField.setPromptText("Производитель");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Кол-во (шт.)");
        TextField purchasePriceField = new TextField();
        purchasePriceField.setPromptText("Закупочная цена");
        TextField sellPriceField = new TextField();
        sellPriceField.setPromptText("Цена продажи");
        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("Пример 2025-03-12");

        grid.add(new Label("Наименование:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Категория:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("ID производителя:"), 0, 2);
        grid.add(idCompanyProducerField, 1, 2);
        grid.add(new Label("Кол-во (шт.):"), 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(new Label("Закупочная цена"), 0, 4);
        grid.add(purchasePriceField, 1, 4);
        grid.add(new Label("Цена продажи"), 0, 5);
        grid.add(sellPriceField, 1, 5);
        grid.add(new Label("Дата истечения срока годности"), 0, 6);
        grid.add(expiryDateField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Преобразуем результат в объект Medicine при нажатии Save
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Medicine medicine = new Medicine();
                medicine.setName(nameField.getText());
                medicine.setCategory(categoryField.getText());
                medicine.setId_company_producer(Integer.parseInt(idCompanyProducerField.getText()));
                medicine.setQuantity(Integer.parseInt(quantityField.getText()));
                medicine.setPurchase_price(Double.parseDouble(purchasePriceField.getText()));
                medicine.setSelling_price(Double.parseDouble(sellPriceField.getText()));
                medicine.setExpiry_date(LocalDate.parse(expiryDateField.getText()));

                return medicine;
            }
            return null;
        });

        Optional<Medicine> result = dialog.showAndWait();

        result.ifPresent(medicine -> {
            try {
                // Сохраняем товар через DAO
                Medicine savedMedicine = MedicineDao.getInstance().save(medicine);
                medicinesData.add(savedMedicine);
                medicineTable.refresh();
                showAlert("Успех", "Товар успешно добавлен", Alert.AlertType.INFORMATION);
            } catch (DaoException e) {
                showAlert("Ошибка", "Не удалось сохранить товар: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void handleDeletionError(DaoException e, Medicine medicine) {
        String errorMessage;
        if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
            errorMessage = "Невозможно удалить товар - есть связанные продажи";
        } else {
            errorMessage = "Ошибка базы данных: " + e.getMessage();
        }
        showAlert("Ошибка", errorMessage, Alert.AlertType.ERROR);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
