package ru.practicum.shareit.item.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OwnerItemDto {
    private Long id;
    private String name;
    private Long ownerId;
}
