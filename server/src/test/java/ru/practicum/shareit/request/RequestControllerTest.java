package ru.practicum.shareit.request;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {

    @Mock
    private ItemRequestService requestService;
    @InjectMocks
    private ItemRequestController requestController;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ItemRequestDto itemRequestDto;
    private ItemRequestItemDto itemRequestItemDto;
    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
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
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Request description");
        itemRequest.setCreated(LocalDateTime.now());
    }

    @Test
    void testGetUserItems() throws Exception {
        when(requestService.getUserItems(anyLong())).thenReturn(List.of(itemRequestItemDto));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(requestService, times(1)).getUserItems(anyLong());
    }

    @Test
    void testGetAllRequests() throws Exception {
        when(requestService.getAllRequests()).thenReturn(List.of(itemRequestDto));
        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        verify(requestService, times(1)).getAllRequests();
    }

    @Test
    void testGetRequest() throws Exception {
        when(requestService.getRequest(anyLong())).thenReturn(itemRequestItemDto);
        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(requestService, times(1)).getRequest(anyLong());
    }

}