package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;


@Transactional
@SpringBootTest(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserService userService;
    private final UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User("user", "user@name.com");

        UserDto servUser = userService.saveUser(user);
        assertThat(servUser.getId(), notNullValue());
        assertThat(servUser.getName(), equalTo(user.getName()));
        assertThat(servUser.getEmail(), equalTo(user.getEmail()));

        User repoUser = userRepository.findById(servUser.getId()).orElseThrow();
        assertThat(repoUser.getName(), equalTo(user.getName()));
        assertThat(repoUser.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    public void testDeleteUser() {
        User user = new User("user", "user@name.com");
        UserDto servUser = userService.saveUser(user);

        userService.deleteUser(servUser.getId());

        User deletedUser = userRepository.findById(servUser.getId()).orElse(null);
        assertThat(deletedUser, nullValue());
    }
}