package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImplTest {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRequestService service;

    @Test
    void testSaveRequest() {
        User user = new User("some@email.com", "Пётр");
        userRepository.save(user);

        ItemRequest request = new ItemRequest("desc");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
        service.addRequest(1L,request);

        ItemRequest repoRequest = repository.findById(1L).get();
        repoRequest.setRequestor(userRepository.findById(1L).get());

        assertThat(repoRequest.getId(),notNullValue());
        assertThat(repoRequest.getDescription(),equalTo(request.getDescription()));
        assertThat(repoRequest.getRequestor(), equalTo(request.getRequestor()));
        assertThat(repoRequest.getCreated(), notNullValue());

    }
}
