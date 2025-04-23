package pharmacy_store.entity;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Sale {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty id_customer = new SimpleIntegerProperty();
    private final DoubleProperty total_cost = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();

    public IntegerProperty idProperty() {return id;}
    public IntegerProperty idCustomerProperty() {return id_customer;}
    public DoubleProperty totalCostProperty() {return total_cost;}
    public ObjectProperty<LocalDate> dateProperty() {return date;}

    public Sale() {
    }

    public int getId() {
        return id.get();
    }
    public void setId(int id) {this.id.set(id);}

    public Integer getId_customer() {
        return id_customer.get();
    }
    public void setId_customer(Integer id_customer) {this.id_customer.set(id_customer);}

    public double getTotal_cost() {
        return total_cost.get();
    }
    public void setTotal_cost(double total_cost) {this.total_cost.set(total_cost);}

    public LocalDate getDate() {
        return date.get();
    }
    public void setDate(LocalDate date) {this.date.set(date);}

    @Override
    public String toString() {
        return "Sale{" +
               "id=" + id +
               ", id_customer=" + id_customer +
               ", total_cost=" + total_cost +
               ", date=" + date +
               '}';
    }
}
