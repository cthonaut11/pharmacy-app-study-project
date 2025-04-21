package pharmacy_store.entity;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Medicine {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final IntegerProperty id_company_producer = new SimpleIntegerProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty purchase_price = new SimpleDoubleProperty();
    private final DoubleProperty selling_price = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> expiry_date = new SimpleObjectProperty<>();

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty categoryProperty() { return category; }
    public IntegerProperty id_company_producerProperty() { return id_company_producer; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty purchasePriceProperty() { return purchase_price; }
    public DoubleProperty sellingPriceProperty() { return selling_price; }
    public ObjectProperty<LocalDate> expirationDateProperty() { return expiry_date; }

    public Medicine() {
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public String getCategory() {
        return category.get();
    }
    public void setCategory(String category) {
        this.category.set(category);
    }

    public int getId_company_producer() {
        return id_company_producer.get();
    }
    public void setId_company_producer(int id_company_producer) {
        this.id_company_producer.set(id_company_producer);
    }

    public int getQuantity() {
        return quantity.get();
    }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getPurchase_price() {
        return purchase_price.get();
    }
    public void setPurchase_price(double purchase_price) {
        this.purchase_price.set(purchase_price);
    }

    public double getSelling_price() {
        return selling_price.get();
    }
    public void setSelling_price(double selling_price) {
        this.selling_price.set(selling_price);
    }

    public LocalDate getExpiry_date() {
        return expiry_date.get();
    }
    public void setExpiry_date(LocalDate expiry_date) {
        this.expiry_date.set(expiry_date);
    }

    @Override
    public String toString() {
        return "Medicine{" +
               "id=" + id +
               ", name=" + name +
               ", category=" + category +
               ", id_company_producer=" + id_company_producer +
               ", quantity=" + quantity +
               ", purchase_price=" + purchase_price +
               ", selling_price=" + selling_price +
               ", expiry_date=" + expiry_date +
               '}';
    }
}
