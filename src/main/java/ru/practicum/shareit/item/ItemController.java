package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        return itemService.getItems(userId);
    }

    @Validated
    @PostMapping
    public ItemDto add(@RequestHeader(name = "X-Sharer-User-Id") @Positive Long userId,
                    @RequestBody @Valid Item item) {
        log.info("request to add item {} from userId = {}", item, userId);
        return itemService.addNewItem(userId, item);
    }

    @Validated
    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                           @PathVariable(name = "itemId") long itemId) {
        itemService.deleteItem(userId, itemId);
    }
}
