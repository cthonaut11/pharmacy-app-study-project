package pharmacy_store.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pharmacy_store.dao.CustomerDao;
import pharmacy_store.dao.MedicineDao;
import pharmacy_store.dao.SaleDao;
import pharmacy_store.dao.SaleItemDao;
import pharmacy_store.entity.*;
import pharmacy_store.exception.DaoException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class SaleController {
    public Button storageButton;
    @FXML private ComboBox<Customer> customerCombo;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> medicineColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Double> sumColumn;
    @FXML private Label totalLabel;

    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private final SaleDao saleDao = SaleDao.getInstance();
    private final SaleItemDao saleItemDao = SaleItemDao.getInstance();
    private final MedicineDao medicineDao = MedicineDao.getInstance();

    @FXML
    public void initialize() {
        configureTableColumns();
        loadCustomers();
    }

    private Stage stage;
    private Scene scene;
    @FXML
    private AnchorPane root;

    public void switchToSceneMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void configureTableColumns() {
        // Настройка привязки колонок к свойствам CartItem
        medicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));

        // Форматирование для денежных колонок
        priceColumn.setCellFactory(column -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("%.2f", price));
            }
        });

        sumColumn.setCellFactory(column -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double sum, boolean empty) {
                super.updateItem(sum, empty);
                setText(empty ? "" : String.format("%.2f", sum));
            }
        });

        cartTable.setItems(cartItems);
    }

    // Загрузка клиентов
    private void loadCustomers() {
        try {
            customerCombo.getItems().setAll(CustomerDao.getInstance().getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void addToCart() {
        Optional<Medicine> selectedMedicine = showMedicineSelectionDialog();

        selectedMedicine.ifPresent(medicine -> {
            int availableQuantity = medicine.getQuantity(); // Получаем доступное количество
            Optional<Integer> quantity = showQuantityInputDialog(medicine, availableQuantity);

            quantity.ifPresent(qty -> {
                if (qty > 0 && qty <= availableQuantity) {
                    cartItems.add(new CartItem(
                            medicine.getId(),
                            medicine.getName(),
                            qty,
                            medicine.getSelling_price()
                    ));
                    updateTotal();
                } else {
                    showAlert("Ошибка", "Недостаточное количество товара", Alert.AlertType.ERROR);
                }
            });
        });
    }

    @FXML
    private void completeSale() {
        if (cartItems.isEmpty()) {
            showAlert("Ошибка", "Добавьте товары в корзину", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Создаем продажу
            Sale sale = new Sale();

            if(customerCombo.getValue() != null && customerCombo.getValue().getId() > 0){
                sale.setId_customer(customerCombo.getValue().getId());
            }

            sale.setTotal_cost(calculateTotal());
            sale.setDate(LocalDate.now());

            // Сохраняем продажу
            Sale savedSale = saleDao.save(sale);

            // Сохраняем позиции и обновляем остатки
            for (CartItem item : cartItems) {
                // Сохраняем позицию
                SaleItem saleItem = new SaleItem();
                saleItem.setId_sale(savedSale.getId());
                saleItem.setId_medicine(item.getMedicineId());
                saleItem.setQuantity(item.getQuantity());
                saleItem.setPrice(item.getPrice());
                saleItemDao.save(saleItem);

                // Обновляем остатки товара
                Medicine medicine = medicineDao.findById(item.getMedicineId());
                medicine.setQuantity(medicine.getQuantity() - item.getQuantity());
                medicineDao.update(medicine);
            }

            showAlert("Успех", "Продажа оформлена!\nНомер продажи: " + savedSale.getId(), Alert.AlertType.INFORMATION);
            cartItems.clear();
            updateTotal();
        } catch (DaoException e) {
            showAlert("Ошибка", "Ошибка при оформлении продажи: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private double calculateTotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private void updateTotal() {
        totalLabel.setText(String.format("Итого: %.2f руб.", calculateTotal()));
    }

    private Optional<Medicine> showMedicineSelectionDialog() {
        Dialog<Medicine> dialog = new Dialog<>();
        dialog.setTitle("Выбор товара");

        // Создаем таблицу для отображения товаров
        TableView<Medicine> tableView = new TableView<>();

        // Колонки таблицы
        TableColumn<Medicine, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Medicine, Double> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        priceCol.setCellFactory(col -> new TableCell<Medicine, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty ? "" : String.format("%.2f", price));
            }
        });

        TableColumn<Medicine, Integer> quantityCol = new TableColumn<>("Доступно");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableView.getColumns().addAll(nameCol, priceCol, quantityCol);

        try {
            tableView.getItems().setAll(MedicineDao.getInstance().getAllMedicine());
        } catch (DaoException | SQLException e) {
            showAlert("Ошибка", "Не удалось загрузить товары", Alert.AlertType.ERROR);
            return Optional.empty();
        }

        dialog.getDialogPane().setContent(tableView);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Настройка выбора
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return tableView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private Optional<Integer> showQuantityInputDialog(Medicine medicine, int maxQuantity) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Количество");
        dialog.setHeaderText(String.format("Товар: %s\nЦена: %.2f руб.\nДоступно: %d",
                medicine.getName(), medicine.getSelling_price(), maxQuantity));
        dialog.setContentText("Введите количество:");

        try {
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                int qty = Integer.parseInt(result.get());
                if (qty <= 0) {
                    showAlert("Ошибка", "Количество должно быть больше 0", Alert.AlertType.ERROR);
                    return Optional.empty();
                }
                if (qty > maxQuantity) {
                    showAlert("Ошибка", "Недостаточно товара на складе", Alert.AlertType.ERROR);
                    return Optional.empty();
                }
                return Optional.of(qty);
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите целое число", Alert.AlertType.ERROR);
            return Optional.empty();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class CartItem {
        private final int medicineId;
        private final String medicineName;
        private final int quantity;
        private final double price;

        public CartItem(int medicineId, String medicineName, int quantity, double price) {
            this.medicineId = medicineId;
            this.medicineName = medicineName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getMedicineName() { return medicineName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getSum() { return price * quantity; }

        public int getMedicineId() { return medicineId; }
    }
}