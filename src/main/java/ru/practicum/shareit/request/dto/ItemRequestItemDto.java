package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.OwnerItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestItemDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<OwnerItemDto> items;

    public ItemRequestItemDto(Long id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }
}
