package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(Long userId, ItemRequest request);

    List<ItemRequestItemDto> getUserItems(Long userId);

    List<ItemRequestDto> getAllRequests();

    ItemRequestItemDto getRequest(Long requestId);
}
