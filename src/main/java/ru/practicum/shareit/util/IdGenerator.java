package ru.practicum.shareit.util;

import lombok.Getter;

@Getter
public class IdGenerator {
    private Long id = 1L;

    public void reloadId() {
        id++;
    }
}
