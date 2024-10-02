package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody RequestBookingDto bookingDto) {
        log.info("request to book item {} by user {}", bookingDto.getItemId(), userId);

        return bookingService.save(userId, bookingDto.getItemId(), bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable(name = "bookingId") long bookingId,
                                     @RequestParam(name = "approved") Boolean approved) {
        log.info("request to approve booking {} by user {}", bookingId, userId);
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable(name = "bookingId") long bookingId) {
        log.info("request to approve get {} by user {}", bookingId, userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(name = "state",defaultValue = "ALL") String state) {
        log.info("request to get {} bookings by booker {}",state, userId);
        return bookingService.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(name = "state",defaultValue = "ALL") String state) {
        log.info("request to get {} bookings by owner {}",state, userId);
        return bookingService.getBookingsByOwner(userId, state);
    }

}
