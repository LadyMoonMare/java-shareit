package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.model.Item;

public class ItemBookingMapper {
    public static ItemBookingDto toDto(Item item){
        return ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
