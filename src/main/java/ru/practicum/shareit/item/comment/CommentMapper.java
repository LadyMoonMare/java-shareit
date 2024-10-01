package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.model.Comment;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }
}
