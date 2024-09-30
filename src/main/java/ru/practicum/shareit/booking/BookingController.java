package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.InvalidDataException;

import java.time.Instant;
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
                                 @RequestBody @Valid ItemBookingDto bookingDto) {
        log.info("request to book item {} by user {}", bookingDto.getItemId(), userId);
        return bookingService.save(userId, bookingDto.getItemId(), bookingDto);
    }

    private void validateTime(Booking booking) {
        if (booking.getStart() == booking.getEnd()
                || booking.getEnd().isBefore(ChronoLocalDateTime.from(Instant.now()))
                || booking.getStart().isBefore(ChronoLocalDateTime.from(Instant.now()))) {
            log.warn("Invalid time exception");
            throw new InvalidDataException("Invalid time of start or finish");
        }
    }
}
