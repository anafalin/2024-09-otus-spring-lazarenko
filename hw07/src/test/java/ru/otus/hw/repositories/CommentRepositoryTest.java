package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("JPA Репозиторий для работы с комментариями книг ")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    private List<Book> dbBooks;

    private Map<Long, List<Comment>> dbCommentsByBookId;

    @BeforeEach
    void setUp() {
        dbBooks = GeneratorData.getDbBooks();
        dbCommentsByBookId = GeneratorData.getDbMapCommentsByIdBook();
    }

    @DisplayName("должен возвращать пустой список комментариев при поиске по id книги")
    @Test
    void findAllByBookId_whenCommentsNotExist_thenEmptyCommentList() {
        Book book = dbBooks.get(2);
        var actualComments = commentRepository.findAllByBookId(book.getId());

        assertThat(actualComments).isEmpty();
    }

    @DisplayName("должен возвращать не пустой список комментариев при поиске по id книги")
    @Test
    void findAllByBookId_whenCommentsExist_thenNotEmptyCommentList() {
        Book book = dbBooks.get(0);
        var actualComments = commentRepository.findAllByBookId(book.getId());
        var expectedComments = dbCommentsByBookId.get(book.getId());

        assertAll(
                () -> assertThat(actualComments).isNotEmpty(),
                () -> assertThat(actualComments.size()).isEqualTo(expectedComments.size()),
                () -> assertThat(actualComments).containsExactlyElementsOf(expectedComments));
    }
}