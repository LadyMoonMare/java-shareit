package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

public interface BookingService {
    BookingDto save(Long userId, Long itemId, RequestBookingDto bookingDto);

    BookingDto approve(Long userId, Long bookingId, boolean approved);
}
