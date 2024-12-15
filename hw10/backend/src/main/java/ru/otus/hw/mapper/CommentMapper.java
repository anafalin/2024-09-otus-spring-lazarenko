package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.comment.CommentCreateUpdateResponse;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentDtoResponse;
import ru.otus.hw.models.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "book.id", target = "bookId")
    CommentDto toCommentDto(Comment comment);

    @Mapping(source = "book.id", target = "bookId")
    List<CommentDto> toCommentDtos(List<Comment> comments);

    CommentCreateUpdateResponse toCommentCreateUpdateResponse(CommentDto commentDto);

    CommentDtoResponse toCommentDtoResponse(CommentDto commentDto);

    List<CommentDtoResponse> toCommentDtoResponseList(List<CommentDto> commentDtoList);
}