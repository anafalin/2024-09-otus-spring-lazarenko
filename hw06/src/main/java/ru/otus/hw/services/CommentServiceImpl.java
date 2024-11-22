package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.map(commentMapper::toCommentDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByBookId(long bookId) {
        List<Comment> comments = commentRepository.findAllByBookId(bookId);
        return commentMapper.toCommentDtos(comments);
    }

    @Override
    @Transactional
    public CommentDto insert(long bookId, String text) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id='%s' not found".formatted(bookId)));

        Comment comment = Comment.builder()
                .text(text)
                .book(book)
                .build();

        comment = commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentDto update(long id, String text) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id='%s' not found".formatted(id)));
        comment.setText(text);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}