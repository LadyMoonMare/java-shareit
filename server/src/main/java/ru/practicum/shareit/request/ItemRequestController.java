package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @Validated
    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                         @RequestBody @Valid ItemRequest request){
        log.info("request to add itemRequest by user{}", userId);
        return requestService.addRequest(userId,request);
    }

    @Validated
    @GetMapping
    public List<ItemRequestItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
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
    public ItemRequestItemDto getRequest(@PathVariable @Positive Long requestId) {
        log.info("request to get itemRequest {}", requestId);
        return requestService.getRequest(requestId);
    }
}
