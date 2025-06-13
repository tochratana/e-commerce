package mapper;

import model.dto.product.AddToCartDto;
import model.entities.CartItem;
import model.entities.Product;

public class AddToCartMapper {
    public static AddToCartDto mapProductToDto(Product product, int quantity) {
        return new AddToCartDto(product.getUuid().toString(), quantity);
    }

    public static CartItem mapDtoToCartItem(Product product, AddToCartDto dto) {
        return new CartItem(
                product.getUuid().toString(),
                product.getName(),
                product.getPrice(),
                dto.getQuantity()
        );
    }
}
