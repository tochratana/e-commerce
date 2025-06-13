package model.entities;

public class CartItem {
    private String productUuid;
    private String productName;
    private double price;
    private int quantity;

    public CartItem(String productUuid, String productName, double price, int quantity) {
        this.productUuid = productUuid.toString();
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int qty) {
        this.quantity += qty;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}