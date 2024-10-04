package ru.practicum.shareit.booking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(user);
        booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
    }

    @Test
    @Transactional
    void testSave() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        RequestBookingDto bookingDto = RequestBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1L)
                .build();
        BookingDto result = bookingService.save(1L, 1L, bookingDto);
        assertNotNull(result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @Transactional
    void testApprove() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDto result = bookingService.approve(1L, 1L, true);
        assertNotNull(result);
        assertEquals(Status.APPROVED, booking.getStatus());
        verify(bookingRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testGetBookingById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDto result = bookingService.getBookingById(1L, 1L);
        assertNotNull(result);
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookingsByBooker() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findByBooker_Id(anyLong())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getBookingsByBooker(1L, "ALL");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(userRepository, times(1)).findById(anyLong());
        verify(bookingRepository, times(1)).findByBooker_Id(anyLong());
    }

    @Test
    void testGetBookingsByOwner() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.getByOwner_Id(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findByOwner_Id(anyLong())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getBookingsByOwner(1L, "ALL");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).getByOwner_Id(anyLong());
        verify(bookingRepository, times(1)).findByOwner_Id(anyLong());
    }

}