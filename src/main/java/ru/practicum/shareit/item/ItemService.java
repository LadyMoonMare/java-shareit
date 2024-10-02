package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<BookingItemDto> getItems(Long userId);

    BookingItemDto getItemById(Long itemId);

    RequestItemDto addNewItem(Long userId, RequestItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, Item item);

    List<ItemDto> searchForItems(String text);

    CommentDto addComment(Long userId,Long itemId, Comment comment);
}
