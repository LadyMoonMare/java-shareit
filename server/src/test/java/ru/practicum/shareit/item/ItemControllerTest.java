package ru.practicum.shareit.item;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookingItemDto bookingItemDto;
    private RequestItemDto requestItemDto;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private Item item;
    private Comment comment;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        bookingItemDto = BookingItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .build();
        requestItemDto = RequestItemDto.builder()
                .id(1L)
                .name("Item name")
                .description("Item description")
                .available(true)
                .requestId(1L)
                .build();
        itemDto = ItemDto.builder()
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
        item = new Item();
        item.setId(1L);
        item.setName("Item name");
        item.setDescription("Item description");
        item.setAvailable(true);
        comment = new Comment();
        comment.setId(1L);
        comment.setText("Comment text");
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    void testGetItems() throws Exception {
        when(itemService.getItems(anyLong())).thenReturn(List.of(bookingItemDto));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(itemService, times(1)).getItems(anyLong());
    }

    @Test
    void testGetItem() throws Exception {
        when(itemService.getItemById(anyLong())).thenReturn(bookingItemDto);
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(itemService, times(1)).getItemById(anyLong());
    }

    @Test
    void testAddItem() throws Exception {
        when(itemService.addNewItem(anyLong(), any(RequestItemDto.class))).thenReturn(requestItemDto);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(itemService, times(1)).addNewItem(anyLong(), any(RequestItemDto.class));
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(Item.class))).thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(itemService, times(1)).updateItem(anyLong(), anyLong(), any(Item.class));
    }

    @Test
    void testSearchItems() throws Exception {
        when(itemService.searchForItems(anyString())).thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items/search")
                        .param("text", "Item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(itemService, times(1)).searchForItems(anyString());
    }

}