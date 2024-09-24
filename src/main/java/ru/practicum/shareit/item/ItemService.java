package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems(Long userId);

    ItemDto getItemById(Long itemId);

    ItemDto addNewItem(Long userId, Item item);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    List<ItemDto> searchForItems(String text);
}
