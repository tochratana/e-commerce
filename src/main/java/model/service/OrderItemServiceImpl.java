package model.service;


import model.dto.OrderItemDto;
import model.entities.OrderItem;
import model.repositories.OrderItemRepository;
import service.OrderItemService;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void addItemToOrder(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItemCode(orderItemDto.itemCode());
        orderItem.setOrderCode(orderItemDto.orderCode());
        orderItem.setProductId(orderItemDto.productId());
        orderItem.setQuantity(orderItemDto.quantity());
        orderItem.setPrice(orderItemDto.price());
        orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItemDto> getItemsByOrderCode(String orderCode) {
        return orderItemRepository.findByOrderCode(orderCode).stream()
                .map(item -> new OrderItemDto(
                        item.getItemCode(),
                        item.getOrderCode(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItem(String itemCode) {
        orderItemRepository.deleteByItemCode(itemCode);
    }
}