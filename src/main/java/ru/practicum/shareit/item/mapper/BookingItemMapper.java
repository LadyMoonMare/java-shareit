package ru.practicum.shareit.item.mapper;


import ru.practicum.shareit.item.dto.BookingItemDto;
import ru.practicum.shareit.item.model.Item;

public class BookingItemMapper {
    public static BookingItemDto toDto(Item item){
        return BookingItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
