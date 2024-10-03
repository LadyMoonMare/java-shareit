package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImplTest {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ItemRequestService service;

    @Test
    void testSaveRequest() {
        User user = new User("some@email.com", "Пётр");
        User servUser = userRepository.save(user);

        ItemRequest request = new ItemRequest("desc");
        request.setRequestor(servUser);
        request.setCreated(LocalDateTime.now());

        ItemRequestDto servRequest = service.addRequest(servUser.getId(),request);
        assertThat(servRequest.getId(),notNullValue());
        assertThat(servRequest.getDescription(),equalTo(request.getDescription()));
        assertThat(servRequest.getCreated(), notNullValue());

        ItemRequest repoRequest = repository.findById(servRequest.getId()).orElseThrow();
        assertThat(repoRequest.getDescription(),equalTo(servRequest.getDescription()));
        assertThat(repoRequest.getCreated(),equalTo(servRequest.getCreated()));

    }
}
