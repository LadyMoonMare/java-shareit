package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto save(Long userId, Long itemId, RequestBookingDto bookingDto);

    BookingDto approve(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBooker(Long userId, String state);

    List<BookingDto> getBookingsByOwner(Long userId, String state);
}
