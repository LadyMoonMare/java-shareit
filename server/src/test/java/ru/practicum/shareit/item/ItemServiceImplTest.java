package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemService service;

    @Test
    void testSaveItem() {
        User user = new User("some@email.com", "Пётр");
        userRepository.save(user);

        RequestItemDto item = new RequestItemDto(null,"item", "desc", false,
                null);
        service.addNewItem(1L,item);
        Item repoItem = itemRepository.findById(1L).get();
        repoItem.setOwner(userRepository.findById(1L).get());

        assertThat(repoItem.getId(), notNullValue());
        assertThat(repoItem.getName(), equalTo(item.getName()));
        assertThat(repoItem.getDescription(), equalTo(item.getDescription()));
    }
}
