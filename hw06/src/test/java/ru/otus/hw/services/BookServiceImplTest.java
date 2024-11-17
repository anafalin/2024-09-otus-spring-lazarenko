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
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Сервис по работе с книгами ")
@DataJpaTest
@Import({BookServiceImpl.class, JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = GeneratorData.getDbAuthors();
        dbGenres = GeneratorData.getDbGenres();
        dbBooks = GeneratorData.getDbBooks(dbAuthors, dbGenres);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен загружать список всех книг")
    @Test
    void findAll() {
        // testing
        var actualBooks = bookService.findAll();

        // verification
        assertThat(actualBooks).isNotEmpty()
                .hasSize(3)
                .hasOnlyElementsOfType(Book.class);
        assertThat(actualBooks)
                .extracting("title")
                .containsExactly("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен загружать книгу по id")
    @Test
    void findById() {
        // input data
        Book expectedBook = dbBooks.get(0);

        // testing
        Optional<Book> actualBook = bookService.findById(expectedBook.getId());

        // verification
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get())
                .hasFieldOrPropertyWithValue("title", "BookTitle_1");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять книгу")
    @Test
    void insert() {
        // input data
        Book expectedBook = new Book(null, "Book_New", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));

        // testing
        Book insertedBook = bookService.insert(
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                Set.copyOf(expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())));

        // verification
        assertNotNull(insertedBook);
        assertThat(insertedBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        var allBooks = bookService.findAll();
        assertThat(allBooks).hasSize(4);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен изменять книгу по id")
    @Test
    void update() {
        // input data
        Book updatableBook = dbBooks.get(0);
        Book expectedBook = new Book(updatableBook.getId(), "Updated Book Title", dbAuthors.get(0),
                List.of(dbGenres.get(3)));

        Optional<Book> optionalBook = bookService.findById(expectedBook.getId());
        assertThat(optionalBook).isPresent();
        assertThat(optionalBook.get()).isEqualTo(updatableBook);

        // testing
        Book updatedBook = bookService.update(updatableBook.getId(), expectedBook.getTitle(),
                expectedBook.getAuthor().getId(), Set.of(dbGenres.get(3).getId()));

        assertNotNull(updatedBook);

        assertThat(updatedBook)
                .usingRecursiveComparison()
                .ignoringFields("author")
                .isEqualTo(expectedBook);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять книгу по id")
    @Test
    void deleteById() {
        // input data
        Book deltableBook = dbBooks.get(0);

        Optional<Book> existingBook = bookService.findById(deltableBook.getId());
        assertThat(existingBook).isPresent();
        assertThat(existingBook.get()).isEqualTo(deltableBook);

        // testing
        bookService.deleteById(deltableBook.getId());

        // verification
        Optional<Book> deletedBook = bookService.findById(deltableBook.getId());
        assertThat(deletedBook).isNotPresent();
    }
}