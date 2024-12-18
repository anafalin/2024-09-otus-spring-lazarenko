package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "book.id", target = "bookId")
    CommentDto toCommentDto(Comment comment);
}