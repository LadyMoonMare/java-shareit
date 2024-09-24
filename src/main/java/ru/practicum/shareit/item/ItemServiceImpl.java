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

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItems(Long userId) {
        return null;
    }

    @Override
    public ItemDto addNewItem(Long userId, Item item) {
        log.info("validation of owner for item {}", item);
        User owner = validateUser(userId);

        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        log.info("validation of existence of item {}", item);
        Item updateItem = validateItem(itemId);
        log.info("validation of existence of supposed owner for item {}", updateItem);
        User supposedOwner = validateUser(userId);
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
    public void deleteItem(Long userId, Long itemId) {

    }

    private User validateUser(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", userId);
            return new NotFoundException("User is not found");
        });
    }

    private Item validateItem(Long itemId) {
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
