package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.comment.CommentCreateUpdateRequest;
import ru.otus.hw.dto.comment.CommentCreateUpdateResponse;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.comment.CommentDtoResponse;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping
    public List<CommentDtoResponse> getAllCommentsByBook(@RequestParam("bookId") long bookId) {
        return commentMapper.toCommentDtoResponseList(commentService.findByBookId(bookId));
    }

    @GetMapping("/{id}")
    public CommentDtoResponse getComment(@PathVariable("id") long id) {
        return commentMapper.toCommentDtoResponse(commentService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentCreateUpdateResponse createComment(@Valid @RequestBody CommentCreateUpdateRequest request) {
        CommentDto savedComment = commentService.insert(request.getBookId(), request.getText());
        return commentMapper.toCommentCreateUpdateResponse(savedComment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable long id) {
        commentService.deleteById(id);
    }

    @PutMapping("/{id}")
    public CommentCreateUpdateResponse update(@PathVariable Long id, @RequestBody CommentCreateUpdateRequest request) {
        CommentDto updatedComment = commentService.update(id, request.getText());
        return commentMapper.toCommentCreateUpdateResponse(updatedComment);
    }
}