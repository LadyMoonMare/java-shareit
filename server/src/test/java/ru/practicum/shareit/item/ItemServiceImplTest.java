package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemService service;


    @Test
    void testSaveItem() {
        User user = new User("some@email.com", "Пётр");
        User repoUser = userRepository.save(user);

        RequestItemDto item = new RequestItemDto(null,"name","desc",true);
        RequestItemDto servItem = service.addNewItem(repoUser.getId(), item);
        assertThat(servItem.getId(), notNullValue());
        assertThat(servItem.getName(), equalTo(item.getName()));
        assertThat(servItem.getDescription(), equalTo(item.getDescription()));
        assertThat(servItem.getAvailable(), equalTo(item.getAvailable()));

        Item repoItem = itemRepository.findById(servItem.getId()).orElseThrow();
        assertThat(repoItem.getName(), equalTo((servItem.getName())));
        assertThat(repoItem.getDescription(), equalTo((servItem.getDescription())));
        assertThat(repoItem.getAvailable(), equalTo(servItem.getAvailable()));
    }
}
