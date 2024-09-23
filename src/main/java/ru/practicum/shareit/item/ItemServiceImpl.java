package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
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
    public List<Item> getItems(Long userId) {
        return null;
    }

    @Override
    public Item addNewItem(Long userId, Item item) {
        log.info("validation of owner for item {}", item);
        User owner = userRepository.findUserById(userId).orElseThrow(() -> {
            log.warn("user with id =  {} is not existing", userId);
            return new NotFoundException("User is not found");
        });
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {

    }
}
