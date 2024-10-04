package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody ItemRequest request) {
        log.info("request to add itemRequest by user{}", userId);
        return requestService.addRequest(userId,request);
    }

    @GetMapping
    public List<ItemRequestItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("request to get all itemRequests by user {}", userId);
        return requestService.getUserItems(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        log.info("request to get all itemRequests");
        return requestService.getAllRequests();
    }

    @Validated
    @GetMapping("/{requestId}")
    public ItemRequestItemDto getRequest(@PathVariable Long requestId) {
        log.info("request to get itemRequest {}", requestId);
        return requestService.getRequest(requestId);
    }
}
