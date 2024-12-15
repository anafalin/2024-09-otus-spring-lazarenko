package ru.otus.hw.services;

import ru.otus.hw.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findByBookId(long bookId);

    CommentDto insert(long bookId, String text);

    CommentDto update(long id, String text);

    void deleteById(long id);
}