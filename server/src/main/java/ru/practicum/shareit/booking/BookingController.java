package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @Validated
    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                 @RequestBody @Valid RequestBookingDto bookingDto) {
        log.info("request to book item {} by user {}", bookingDto.getItemId(), userId);

        return bookingService.save(userId, bookingDto.getItemId(), bookingDto);
    }

    @Validated
    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                     @PathVariable(name = "bookingId") @Positive long bookingId,
                                     @RequestParam(name = "approved") @NotNull Boolean approved) {
        log.info("request to approve booking {} by user {}", bookingId, userId);
        return bookingService.approve(userId, bookingId, approved);
    }

    @Validated
    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                 @PathVariable(name = "bookingId") @Positive long bookingId) {
        log.info("request to approve get {} by user {}", bookingId, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @Validated
    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                        @RequestParam(name = "state",defaultValue = "ALL") String state) {
        log.info("request to get {} bookings by booker {}",state, userId);
        return bookingService.getBookingsByBooker(userId, state);
    }

    @Validated
    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                        @RequestParam(name = "state",defaultValue = "ALL") String state) {
        log.info("request to get {} bookings by owner {}",state, userId);
        return bookingService.getBookingsByOwner(userId, state);
    }

}
