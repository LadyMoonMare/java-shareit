package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                         @RequestBody @Valid RequestDto request){
        log.info("request to add itemRequest by user{}", userId);
        return requestClient.addRequest(userId,request);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("request to get all itemRequests by user {}", userId);
        return requestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        log.info("request to get all itemRequests");
        return requestClient.getAllRequests();
    }

    @Validated
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@PathVariable @Positive Long requestId) {
        log.info("request to get itemRequest {}", requestId);
        return requestClient.getRequest(requestId);
    }
}
