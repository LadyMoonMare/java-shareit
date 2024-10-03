package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    private final BookingService service;
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    void testAddBooking() {
        User user = new User("some@email.com", "Пётр");
        User repoUser = userRepository.save(user);

        Item item = new Item("name","desc",true,repoUser.getId());
        item.setOwner(repoUser);
        Item repoItem = itemRepository.save(item);

        RequestBookingDto booking = new RequestBookingDto(LocalDateTime.parse("2024-11-01 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse("2024-12-01 00:00:00",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),repoItem.getId());
        BookingDto servBooking = service.save(repoUser.getId(), repoItem.getId(), booking);
        assertThat(servBooking.getId(), notNullValue());
        assertThat(servBooking.getStart(),equalTo(booking.getStart()));
        assertThat(servBooking.getEnd(), equalTo(booking.getEnd()));
        assertThat(servBooking.getStatus(),equalTo(Status.WAITING));

        Booking repoBooking = repository.findById(servBooking.getId()).orElseThrow();
        assertThat(repoBooking.getStart(),equalTo(servBooking.getStart()));
        assertThat(repoBooking.getEnd(), equalTo(servBooking.getEnd()));
        assertThat(repoBooking.getStatus(),equalTo(servBooking.getStatus()));
    }

}
