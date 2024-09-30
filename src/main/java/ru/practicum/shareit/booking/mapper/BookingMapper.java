package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.mapper.BookingItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;

public class BookingMapper {
    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(LocalDateTime.from(booking.getStart()))
                .end(LocalDateTime.from(booking.getEnd()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .item(BookingItemMapper.toDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }

    public static Booking fromItemBookingDto(RequestBookingDto dto) {
        return new Booking(dto.getStart(), dto.getEnd());
    }
}
