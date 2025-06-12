package model.repositories;

import model.entities.Order;

import java.util.List;
import java.util.Optional;

public class OrderRepository implements Repository<Order, Integer>{
    @Override
    public Order save(Order entity) {
        return null;
    }

    @Override
    public Optional<Order> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public Order update(Order entity) {
        return null;
    }
}
