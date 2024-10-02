package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.NameIdItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.OwnerItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {
    //method of mapping item entity to dto object through builder
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static NameIdItemDto toDto(Item item) {
        return NameIdItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public static OwnerItemDto toOwnerItemDto(Item item) {
        return OwnerItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public static Item fromRequestItemDto(RequestItemDto dto) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public static RequestItemDto toRequestItemDto(Item item) {
        RequestItemDto dto = new RequestItemDto(item.getId(), item.getName(), item.getDescription(),
        item.getAvailable());
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }
        return dto;
    }
}
