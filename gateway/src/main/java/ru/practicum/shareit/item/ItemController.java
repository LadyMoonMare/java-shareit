package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable @Positive long itemId) {
        return itemClient.getItem(itemId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(name = "X-Sharer-User-Id") @Positive Long userId,
                              @RequestBody @Valid RequestItemDto itemDto) {
        log.info("adding item {} from userId = {}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(name = "X-Sharer-User-Id") @Positive Long userId,
                          @PathVariable @Positive Long itemId, @RequestBody ItemDto item) {
        log.info("validating updating item params");
        if ((item.getName() != null) && (item.getName().isEmpty() || item.getName().isBlank())) {
            log.warn("invalid updating item name");
            throw new InvalidDataException("invalid item name");
        }
        if ((item.getDescription() != null) && (item.getDescription().isEmpty()
                || item.getDescription().isBlank())) {
            log.warn("invalid updating item description");
            throw new InvalidDataException("invalid item description");
        }
        log.info("request to add item {} from userId = {}", item, userId);
        return itemClient.updateItem(itemId, item, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") @NotBlank String text) {
        log.info("request to search text {} in item list", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(name = "X-Sharer-User-Id")
                                                 @Positive Long userId,
                                 @PathVariable @Positive Long itemId,
                                 @RequestBody @Valid CommentDto comment) {
        log.info("request to add comment to item {} from user {}", itemId, userId);
        return itemClient.addComment(userId,itemId, comment);
    }
}
