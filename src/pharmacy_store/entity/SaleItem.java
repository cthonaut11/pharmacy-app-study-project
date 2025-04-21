package pharmacy_store.entity;

import javafx.beans.property.*;

public class SaleItem {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty id_sale = new SimpleIntegerProperty();
    private final IntegerProperty id_medicine = new SimpleIntegerProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();

    public IntegerProperty idProperty() {return id;}
    public IntegerProperty idSaleProperty() {return id_sale;}
    public IntegerProperty idMedicineProperty() {return id_medicine;}
    public IntegerProperty quantityProperty() {return quantity;}
    public DoubleProperty priceProperty() {return price;}

    public SaleItem() {
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {this.id.set(id);}

    public int getId_sale() {
        return id_sale.get();
    }
    public void setId_sale(int id_sale) {this.id_sale.set(id_sale);}

    public int getId_medicine() {return id_medicine.get();}
    public void setId_medicine(int id_medicine) {this.id_medicine.set(id_medicine);}

    public int getQuantity() {
        return quantity.get();
    }
    public void setQuantity(int quantity) {this.quantity.set(quantity);}

    public double getPrice() {
        return price.get();
    }
    public void setPrice(double price) {this.price.set(price);}

    @Override
    public String toString() {
        return "SaleItem{" +
               "id=" + id +
               ", id_sale=" + id_sale +
               ", id_medicine=" + id_medicine +
               ", quantity=" + quantity +
               ", price=" + price +
               '}';
    }
}
