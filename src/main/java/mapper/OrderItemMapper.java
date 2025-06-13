package mapper;

import model.dto.order.OrderItemDto;
import model.entities.OrderItem;

public class OrderItemMapper {
    public static OrderItemDto toDTO(OrderItem item) {
        return new OrderItemDto(
                item.getOrderId(),      // Add this if OrderItem has orderId
                item.getProductName(),
                item.getProductPrice(),
                item.getQuantity()
        );
    }
}
