package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request){
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();

    }

    public static ItemRequestItemDto toItemRequestItemDto(ItemRequest request) {
        return new ItemRequestItemDto(request.getId(),
                request.getDescription(),
                request.getCreated());
    }
}
