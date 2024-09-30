package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.mapper.BookingItemMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(LocalDateTime.from(booking.getStart()))
                .end(LocalDateTime.from(booking.getEnd()))
                .booker(booking.getBooker())
                .item(BookingItemMapper.toDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    public static Booking fromItemBookingDto(ItemBookingDto dto) {
        return new Booking(dto.getStart(), dto.getEnd());
    }
}
