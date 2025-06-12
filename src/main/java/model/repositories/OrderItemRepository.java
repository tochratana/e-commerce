package model.repositories;

import model.entities.OrderItem;

import java.util.List;
import java.util.Optional;

public class OrderItemRepository implements Repository<OrderItem, Integer>{
    @Override
    public OrderItem save(OrderItem entity) {
        return null;
    }

    @Override
    public Optional<OrderItem> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public OrderItem update(OrderItem entity) {
        return null;
    }
}
