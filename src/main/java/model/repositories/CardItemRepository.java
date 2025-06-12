package model.repositories;

import model.entities.CardItem;

import java.util.List;
import java.util.Optional;

public class CardItemRepository implements Repository<CardItem, Integer>{
    @Override
    public CardItem save(CardItem entity) {
        return null;
    }

    @Override
    public Optional<CardItem> findById(Integer integer) {
        return null;
    }

    @Override
    public List<CardItem> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public CardItem update(CardItem entity) {
        return null;
    }
}
