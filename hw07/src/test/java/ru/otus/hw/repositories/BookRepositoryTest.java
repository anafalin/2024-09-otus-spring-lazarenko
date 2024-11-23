package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA Репозиторий для работы с книгами ")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

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

    @DisplayName("должен возвращать optional c книгой при поиске по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findById_whenBookExists_thenNotEmptyBookOptional(Book expectedBook) {
        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен возвращать пустой optional при поиске по id")
    @Test
    void findById_whenBookNotExist_thenEmptyBookOptional() {
        Optional<Book> result = bookRepository.findById(100L);
        assertThat(result).isEmpty();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAll_whenBooksExist_thenNotEmptyBookList() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    private static List<Book> getDbBooks() {
        return GeneratorData.getDbBooks();
    }
}