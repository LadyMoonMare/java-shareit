package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
class ItemController {
    private final ItemService itemService;

    @Validated
    @GetMapping
    public List<BookingItemDto> get(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getItems(userId);
    }

    @Validated
    @GetMapping("/{itemId}")
    public BookingItemDto getItem(@PathVariable @Positive long itemId) {
        return itemService.getItemById(itemId);
    }

    @Validated
    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") @Positive Long userId,
                    @RequestBody @Valid Item item) {
        log.info("request to add item {} from userId = {}", item, userId);
        return itemService.addNewItem(userId, item);
    }

    @Validated
    @PatchMapping("/{itemId}")
    public  ItemDto update(@RequestHeader(name = "X-Sharer-User-Id") @Positive Long userId,
            @PathVariable @Positive Long itemId, @RequestBody Item item) {
        log.info("request to add item {} from userId = {}", item, userId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("request to search text {} in item list", text);
        return itemService.searchForItems(text);
    }

    @Validated
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(name = "X-Sharer-User-Id") @Positive Long userId,
                                 @PathVariable @Positive Long itemId,
                                 @RequestBody Comment comment) {
        log.info("request to add comment to item {} from user {}", itemId, userId);
        return itemService.addComment(userId,itemId, comment);
    }

}
