package model.dto.product;

public class AddToCartDto {
    private String productUuid;
    private int quantity;

    public AddToCartDto(String productUuid, int quantity) {
        this.productUuid = productUuid;
        this.quantity = quantity;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
