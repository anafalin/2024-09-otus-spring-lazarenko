package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.CommentMapperImpl;
import ru.otus.hw.mapper.GenreMapperImpl;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Сервис по работе с комментариями ")
@DataJpaTest
@Import({CommentServiceImpl.class, JpaCommentRepository.class, JpaBookRepository.class, JpaAuthorRepository.class,
        JpaGenreRepository.class,
        CommentMapperImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    private List<Book> dbBooks;

    private List<Comment> dbComments;

    private Map<Long, List<Comment>> dbMapCommentsByIdBook;

    @BeforeEach
    void setUp() {
        dbBooks = GeneratorData.getDbBooks();
        dbComments = GeneratorData.getDbComments();
        dbMapCommentsByIdBook = GeneratorData.getDbMapCommentsByIdBook();
    }

    @DisplayName("должен находить комментарий по id")
    @Test
    void findById_optionalNotEmpty_commentExists() {
        // input data
        Comment expectedComment = dbComments.get(0);

        // testing
        Optional<CommentDto> actualComment = commentService.findById(expectedComment.getId());

        // verification
        assertThat(actualComment).isPresent();
        assertAll(
                () -> assertThat(actualComment.get().getId()).isEqualTo(expectedComment.getId()),
                () -> assertThat(actualComment.get().getText()).isEqualTo(expectedComment.getText()),
                () -> assertThat(actualComment.get().getCreatedAt()).isEqualTo(expectedComment.getCreatedAt())
        );
    }

    @DisplayName("должен находить комментарии по id книги")
    @Test
    void findByBookId_comments_commentsExist() {
        // input data
        Long bookId = dbBooks.get(0).getId();
        List<Comment> expectedComments = dbMapCommentsByIdBook.get(bookId);

        // testing
        List<CommentDto> actualComments = commentService.findByBookId(bookId);

        // verification
        assertAll(
                () -> assertThat(actualComments).isNotEmpty(),
                () -> assertThat(actualComments.size()).isEqualTo(expectedComments.size()),
                () -> assertThat(actualComments.get(0).getId()).isEqualTo(expectedComments.get(0).getId()),
                () -> assertThat(actualComments.get(0).getText()).isEqualTo(expectedComments.get(0).getText()));
    }

    @DisplayName("должен создавать новый комментарий к книге")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void insert_savedNewComment_commentRequestIsCorrect() {
        // input data
        String text = "New Comment to book";
        Book book = dbBooks.get(0);

        // testing
        CommentDto insertedComment = commentService.insert(book.getId(), text);

        // verification
        assertNotNull(insertedComment);
        assertThat(insertedComment.getText()).isEqualTo(text);
        assertThat(insertedComment.getBookId()).isEqualTo(book.getId());
    }

    @DisplayName("должен обновлять комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void update_updatedComment_commentRequestIsCorrect() {
        // input data
        Comment expectedComment = dbComments.get(0);
        String updatedText = "New updated text";

        Optional<CommentDto> optionalComment = commentService.findById(expectedComment.getId());
        assertThat(optionalComment).isPresent();

        // testing
        CommentDto updatedComment = commentService.update(expectedComment.getId(), updatedText);

        // verification
        assertNotNull(updatedComment);
        assertThat(updatedComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(updatedComment.getText()).isEqualTo(updatedText);
    }

    @DisplayName("должен удалять комментарий по id")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void deleteComment_commentWasDeleted_commentExist() {
        // input data
        Comment testingComment = dbComments.get(0);

        Optional<CommentDto> existingComment = commentService.findById(testingComment.getId());
        assertThat(existingComment).isPresent();

        // testing
        commentService.deleteById(testingComment.getId());

        // verification
        Optional<CommentDto> deletedComment = commentService.findById(testingComment.getId());
        assertThat(deletedComment).isNotPresent();
    }
}