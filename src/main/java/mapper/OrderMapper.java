package mapper;

import model.dto.order.OrderDTO;
import model.dto.order.OrderItemDto;
import model.entities.Order;
import model.entities.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO toOrderDTO(Order order) {
        if (order == null) return null;

        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(OrderMapper::toOrderItemDto)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),             // id
                order.getOrderCode(),      // orderCode
                order.getTotalQuantity(),  // totalQuantity
                order.getTotalPrice(),     // totalPrice
                order.getOrderDate(),      // orderDate
                itemDtos                   // items
        );
    }

    public static OrderItemDto toOrderItemDto(OrderItem item) {
        if (item == null) return null;

        return new OrderItemDto(
                item.getOrderId(),         // orderId
                item.getProductName(),     // productName
                item.getProductPrice(),    // productPrice
                item.getQuantity()         // quantity
        );
    }
}
