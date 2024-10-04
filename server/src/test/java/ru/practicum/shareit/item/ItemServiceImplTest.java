package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    private User user;
    private Item item;
    private Booking booking;
    private Comment comment;
    private ItemRequest itemRequest;
    private RequestItemDto requestItemDto;
    private BookingItemDto bookingItemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        item = new Item();
        item.setId(1L);
        item.setName("Item name");
        item.setDescription("Item description");
        item.setAvailable(true);
        item.setOwner(user);
        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        comment = new Comment();
        comment.setId(1L);
        comment.setText("Comment text");
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request description");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        requestItemDto = RequestItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .requestId(1L)
                .build();
        bookingItemDto = BookingItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("Comment text")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    @Transactional
    void testAddNewItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        RequestItemDto result = itemService.addNewItem(1L, requestItemDto);
        assertNotNull(result);
        assertEquals(requestItemDto.getId(), result.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(requestRepository, times(1)).findById(anyLong());
    }

    @Test
    @Transactional
    void testUpdateItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDto result = itemService.updateItem(1L, 1L, item);
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        verify(itemRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testSearchForItems() {
        when(itemRepository.search(anyString())).thenReturn(List.of(item));
        List<ItemDto> result = itemService.searchForItems("Item");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(itemRepository, times(1)).search(anyString());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getItems(1L));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetItemNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L));
        verify(itemRepository, times(1)).findById(anyLong());
    }

}