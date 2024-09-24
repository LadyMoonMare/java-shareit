package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItems(Long userId) {
        log.info("validation of existence of user{}", userId);
        User user = getUser(userId);
        List<Item> items = itemRepository.findByUserId(userId);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto addNewItem(Long userId, Item item) {
        log.info("validation of owner for item {}", item);
        User owner = getUser(userId);

        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        log.info("validation of existence of item {}", item);
        Item updateItem = getItem(itemId);
        log.info("validation of existence of supposed owner for item {}", updateItem);
        User supposedOwner = getUser(userId);
        log.info("validation of owner for item {}", updateItem);
        validateOwner(supposedOwner, updateItem);

        //patching updating entity
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.update(itemId, updateItem));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(getItem(itemId));
    }

    private User getUser(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", userId);
            return new NotFoundException("User is not found");
        });
    }

    private Item getItem(Long itemId) {
        return itemRepository.findItemById(itemId).orElseThrow(() -> {
            log.warn("item with id =  {} is not existing", itemId);
            return new NotFoundException("Item is not found");
        });
    }

    private void validateOwner(User user, Item item) {
        if (!item.getOwner().equals(user)) {
            throw new ForbiddenException("Access is forbidden for users except owner");
        }
    }
}
