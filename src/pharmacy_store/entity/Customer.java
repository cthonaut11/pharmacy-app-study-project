package pharmacy_store.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty first_name = new SimpleStringProperty();
    private final StringProperty last_name = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final IntegerProperty discount = new SimpleIntegerProperty();

    public Customer() {
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirst_name() {
        return first_name.get();
    }
    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }

    public void setLast_name(String last_name) {this.last_name.set(last_name);}
    public String getLast_name() {return last_name.get();}

    public String getPhoneNumber() {
        return phoneNumber.get();
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public int getDiscount() {
        return discount.get();
    }
    public void setDiscount(int discount) {
        this.discount.set(discount);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    public StringProperty first_nameProperty() {
        return first_name;
    }
    public StringProperty last_nameProperty() {return last_name;}
    public StringProperty phone_numberProperty() {
        return phoneNumber;
    }
    public IntegerProperty discountProperty() {
        return discount;
    }

    @Override
    public String toString() {
        return "Customer{" +
               "id=" + id +
               ", first_name=" + first_name +
               ", last_name=" + last_name +
               ", phoneNumber=" + phoneNumber +
               ", discount=" + discount +
               '}';
    }
}
