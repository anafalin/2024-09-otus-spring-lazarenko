package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // cbid 1
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    // acbidb 1
    @ShellMethod(value = "Find all comments by book id", key = "acbidb")
    public String findAllCommentsByBookId(long bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cbins commentToBook 2
    @ShellMethod(value = "Insert comment", key = "cbins")
    public String insertComment(String text, long bookId) {
        var savedComment = commentService.insert(bookId, text);
        return commentConverter.commentToString(savedComment);
    }

    // cbupd updateCommentToBook 2
    @ShellMethod(value = "Update comment", key = "cbupd")
    public String updateComment(String text, long id) {
        var savedComment = commentService.update(id, text);
        return commentConverter.commentToString(savedComment);
    }

    // cbdel 1
    @ShellMethod(value = "Delete comment by id", key = "cbdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}