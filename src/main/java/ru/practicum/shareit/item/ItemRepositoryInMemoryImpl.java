package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRepositoryInMemoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final IdGenerator idGenerator = new IdGenerator();

    @Override
    public List<Item> findByUserId(long userId) {
        return items.values().stream()
                .filter(u -> u.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item save(Item item) {
        log.info("attempt to add to memory item {}", item);
        item.setId(idGenerator.getId());
        log.info("id from generator = {}, userId = {}",idGenerator.getId(), item.getId());
        idGenerator.reloadId();

        items.put(item.getId(), item);
        log.info("user successfully added to memory: {}", items.get(item.getId()));
        return item;
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        log.info("attempt to find item with id {}", itemId);
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item update(Long itemId, Item item) {
        log.info("attempt to update in memory item with id {}", itemId);
        items.put(itemId,item);
        return item;
    }

    @Override
    public List<Item> findItemsByText(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return items.values().stream()
                .filter(i ->
                        i.getName().toLowerCase().contains(text.toLowerCase())
                || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}
