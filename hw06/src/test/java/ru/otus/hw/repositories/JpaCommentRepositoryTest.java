package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.testObjects.GeneratorData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Репозиторий на основе JPA для работы с комментариями книг ")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    private List<Book> dbBooks;

    private Map<Long, List<Comment>> dbCommentsByBookId;

    @BeforeEach
    void setUp() {
        dbBooks = GeneratorData.getDbBooks();
        dbCommentsByBookId = GeneratorData.getDbMapCommentsByIdBook();
    }

    @DisplayName("должен возвращать пустой список комментариев по id книги")
    @Test
    void findAllByBookId_emptyCommentsList_commentsNotExist() {
        Book book = dbBooks.get(2);
        var actualComments = commentRepository.findAllByBookId(book.getId());

        assertThat(actualComments).isEmpty();
    }

    @DisplayName("должен возвращать не пустой список комментариев по id книги")
    @Test
    void findAllByBookId_correctNotEmptyCommentsList_commentsExist() {
        Book book = dbBooks.get(0);
        var actualComments = commentRepository.findAllByBookId(book.getId());
        var expectedComments = dbCommentsByBookId.get(book.getId());

        assertAll(
                () -> assertThat(actualComments).isNotEmpty(),
                () -> assertThat(actualComments.size()).isEqualTo(expectedComments.size()),
                () -> assertThat(actualComments).containsExactlyElementsOf(expectedComments));
    }

    @DisplayName("должен возвращать пустой optional при поиске по id")
    @Test
    void findById_emptyOptional_commentExist() {
        var actualOptionalComment = commentRepository.findById(100L);
        assertThat(actualOptionalComment).isEmpty();
    }

    @DisplayName("должен возвращать не пустой optional при поиске по id")
    @Test
    void findById_notEmptyOptional_commentExist() {
        var expectedComment = dbCommentsByBookId.get(1L).get(0);
        var actualOptionalComment = commentRepository.findById(expectedComment.getId());

        assertThat(actualOptionalComment).isNotEmpty();
        assertThat(actualOptionalComment.get()).isEqualTo(expectedComment);
    }


    @DisplayName("должен сохранять новый комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void save_saveNewComment_requestIsCorrect() {
        var expectedComment = new Comment(null, "Comment to book_1", LocalDate.now(), dbBooks.get(0));

        var returnedComment = commentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(commentRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void save_saveUpdatedComment_commentExists() {
        var book = dbBooks.get(0);
        var expectedComment = new Comment(1L, "Changed Comment to book_1", LocalDate.now(), book);
        assertThat(em.find(Comment.class, expectedComment.getId()))
                .isNotNull();

        var returnedBook = commentRepository.save(expectedComment);
        assertThat(returnedBook).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        Comment returnedComment = em.find(Comment.class, expectedComment.getId());
        assertThat(returnedComment)
                .isEqualTo(expectedComment);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void deleteById() {
        var expectedComment = dbCommentsByBookId.get(1L).get(0);
        var actualComment = em.find(Comment.class, expectedComment.getId());

        assertThat(actualComment).isNotNull();

        commentRepository.deleteById(expectedComment.getId());

        actualComment = em.find(Comment.class, expectedComment.getId());
        assertThat(actualComment).isNull();
    }
}