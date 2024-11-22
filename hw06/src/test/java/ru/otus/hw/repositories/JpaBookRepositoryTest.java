package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaCommentRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private TestEntityManager em;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = GeneratorData.getDbAuthors();
        dbGenres = GeneratorData.getDbGenres();
        dbBooks = GeneratorData.getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findById_correctBookById_bookExists(Book expectedBook) {
        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен возвращать пустой Optional")
    @Test
    void findById_emptyOptional_whenBookNotFound() {
        Optional<Book> result = bookRepository.findById(100);
        assertThat(result).isEmpty();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAll_correctNotEmptyBooksList_booksExist() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void save_saveNewBook_requestIsCorrect() {
        var expectedBook = new Book(null, "BookTitle_10500", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(2)));

        var returnedBook = bookRepository.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        Book book = em.find(Book.class, returnedBook.getId());
        assertTrue(Objects.nonNull(book));
        assertThat(book).isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void save_saveUpdatedBook_bookExists() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2),
                List.of(dbGenres.get(4), dbGenres.get(5)));

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void deleteById_deleteBook_bookExists() {
        assertThat(bookRepository.findById(1L)).isPresent();
        bookRepository.deleteById(1L);
        assertThat(bookRepository.findById(1L)).isEmpty();
    }

    private static List<Book> getDbBooks() {
        return GeneratorData.getDbBooks();
    }
}