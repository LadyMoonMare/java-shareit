package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

interface ItemRepository {

    List<Item> findByUserId(long userId);

    Item save(Item item);

    Optional<Item> findItemById(Long itemId);

    Item update(Long itemId, Item item);

    List<Item> findItemsByText(String text);
}
