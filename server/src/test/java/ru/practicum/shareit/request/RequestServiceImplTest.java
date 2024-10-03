package ru.practicum.shareit.request;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemRequestItemDto itemRequestItemDto;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Request description")
                .created(LocalDateTime.now())
                .build();
        itemRequestItemDto = ItemRequestItemDto.builder()
                .id(1L)
                .description("Request description")
                .created(LocalDateTime.now())
                .items(List.of())
                .build();
        item = new Item();
        item.setId(1L);
        item.setRequest(itemRequest);
    }

    @Test
    @Transactional
    void testAddRequest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        ItemRequestDto result = itemRequestService.addRequest(1L, itemRequest);
        assertNotNull(result);
        assertEquals(itemRequestDto.getId(), result.getId());
        verify(userRepository, times(1)).findById(anyLong());
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void testGetAllRequests() {
        when(requestRepository.findAll()).thenReturn(List.of(itemRequest));
        List<ItemRequestDto> result = itemRequestService.getAllRequests();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getUserItems(1L));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetRequestNotFound() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemRequestService.getRequest(1L));
        verify(requestRepository, times(1)).findById(anyLong());
    }

}