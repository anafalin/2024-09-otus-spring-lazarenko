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
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Сервис по работе с комментариями ")
@DataJpaTest
@Import({BookServiceImpl.class,
        JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class, JpaCommentRepository.class,
        CommentServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    private List<Book> dbBooks;

    private List<Comment> dbComments;

    @BeforeEach
    void setUp() {
        dbBooks = GeneratorData.getDbBooks();
        dbComments = GeneratorData.getDbComments();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен находить комментарий по id")
    @Test
    void shouldReturnCommentById() {
        // input data
        Comment expectedComment = dbComments.get(0);

        // testing
        Optional<Comment> actualComment = commentService.findById(expectedComment.getId());

        // verification
        assertThat(actualComment).isPresent();
        assertThat(actualComment.get())
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComment);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен создавать новый комментарий к книге")
    @Test
    void shouldInsertNewComment() {
        // input data
        String text = "New Comment to book";
        Book book = dbBooks.get(0);

        // testing
        Comment insertedComment = commentService.insert(book.getId(), text);

        // verification
        assertNotNull(insertedComment);
        assertThat(insertedComment.getText()).isEqualTo(text);
        assertThat(insertedComment.getBook().getId()).isEqualTo(book.getId());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен обновлять комментарий по id")
    @Test
    void shouldUpdateComment() {
        // input data
        Comment expectedComment = dbComments.get(0);
        String updatedText = "New updated text";

        Optional<Comment> optionalComment = commentService.findById(expectedComment.getId());
        assertThat(optionalComment).isPresent();

        // testing
        Comment updatedComment = commentService.update(expectedComment.getId(), updatedText);

        // verification
        assertNotNull(updatedComment);
        assertThat(updatedComment.getId()).isEqualTo(expectedComment.getId());
        assertThat(updatedComment.getText()).isEqualTo(updatedText);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять комментарий по id")
    @Test
    void shouldDeleteComment() {
        // input data
        Comment testingComment = dbComments.get(0);

        Optional<Comment> existingComment = commentService.findById(testingComment.getId());
        assertThat(existingComment).isPresent();

        // testing
        commentService.deleteById(testingComment.getId());

        // verification
        Optional<Comment> deletedComment = commentService.findById(testingComment.getId());
        assertThat(deletedComment).isNotPresent();
    }
}