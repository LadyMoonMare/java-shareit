package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    BookingDto save(Long userId, Long itemId, ItemBookingDto bookingDto);
}
