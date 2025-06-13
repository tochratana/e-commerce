package service;


import model.dto.OrderItemDto;

import java.util.List;

public interface OrderItemService {
    void addItemToOrder(OrderItemDto orderItemDto);
    List<OrderItemDto> getItemsByOrderCode(String orderCode);

    void deleteItem(String itemCode);
}