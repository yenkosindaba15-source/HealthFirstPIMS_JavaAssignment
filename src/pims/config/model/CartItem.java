package pims.config.model;

public class CartItem {
    private int medicineId;
    private String medicineName;
    private double price;
    private int quantity;

    public CartItem(int medicineId, String medicineName, double price, int quantity) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return price * quantity;
    }
}