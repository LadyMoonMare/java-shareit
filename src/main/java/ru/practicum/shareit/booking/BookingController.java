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
import ru.practicum.shareit.exception.InvalidDataException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

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
        validateTime(bookingDto);

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

    private void validateTime(RequestBookingDto booking) {
        if (booking.getStart() == booking.getEnd()
                || booking.getEnd().isBefore(LocalDateTime.now())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            log.warn("Invalid time exception");
            throw new InvalidDataException("Invalid time of start or finish");
        }
    }
}
