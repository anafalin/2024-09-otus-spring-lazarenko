package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapperImpl;
import ru.otus.hw.mapper.BookMapperImpl;
import ru.otus.hw.mapper.GenreMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис по работе с книгами ")
@DataJpaTest
@Import({BookServiceImpl.class, BookMapperImpl.class, AuthorMapperImpl.class, GenreMapperImpl.class})
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
    @DisplayName("должен возвращать не пустой список всех книг")
    @Test
    void findAll_whenBookExist_thenNotEmptyBookList() {
        // testing
        var actualBooks = bookService.findAll(PageRequest.of(0, 10));

        // verification
        assertThat(actualBooks).isNotEmpty()
                .hasSize(dbBooks.size())
                .hasOnlyElementsOfType(BookDto.class);
        assertThat(actualBooks)
                .extracting("title")
                .containsExactly("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен возвращать optional с книгой при поиске по id")
    @Test
    void findById_whenBookExists_thenNotEmptyBookOptional() {
        // input data
        Book expectedBook = dbBooks.get(0);

        // testing
        Optional<BookDto> actualBook = bookService.findById(expectedBook.getId());

        // verification
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get())
                .hasFieldOrPropertyWithValue("title", "BookTitle_1");
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен возвращать пустой optional при поиске по id")
    @Test
    void findById_whenBookExists_thenEmptyBookOptional() {
        // input data
        Long notExistIdBook = 100L;

        // testing
        Optional<BookDto> actualBook = bookService.findById(notExistIdBook);

        // verification
        assertThat(actualBook).isEmpty();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен сохранять книгу")
    @Test
    void save_whenCorrectRequest_thenSavesBook() {
        // input data
        Book expectedBook = new Book(null, "Book_New", dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));

        // testing
        BookDto insertedBook = bookService.save(
                expectedBook.getTitle(),
                expectedBook.getAuthor().getId(),
                Set.copyOf(expectedBook.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())));

        // verification
        assertNotNull(insertedBook);
        assertThat(insertedBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);

        var allBooks = bookService.findAll(PageRequest.of(0, 10));
        assertThat(allBooks).hasSize(4);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен бросать исключение, когда при сохранении книги автор не найден")
    @Test
    void save_whenAuthorNotExist_thenEntityNotFoundException() {
        // input data
        String title = "new book insert";
        Long authorNotExistId = 100L;
        Genre genre = dbGenres.get(0);

        // testing
        assertThrows(EntityNotFoundException.class,
                () -> bookService.save(title, authorNotExistId, Set.of(genre.getId())),
                "Author with id='%s' not found".formatted(authorNotExistId));

        // verification
        var allBooks = bookService.findAll(PageRequest.of(0, 10));
        assertThat(allBooks).hasSize(dbBooks.size());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен бросать исключение, когда при сохранении книги" +
            " передаваемое множество id жанров содержит не существующий id")
    @Test
    void save_whenOneOfGenreIdIsNotExist_thenEntityNotFoundException() {
        // input data
        String title = "new book insert";
        Long authorId = dbAuthors.get(0).getId();
        Set<Long> genresIds = Set.of(dbGenres.get(0).getId(), 100L);

        // testing
        assertThrows(EntityNotFoundException.class,
                () -> bookService.save(title, authorId, genresIds),
                "One or all genres with ids = '100L' not found");

        // verification
        var allBooks = bookService.findAll(PageRequest.of(0, 10));
        assertThat(allBooks).hasSize(dbBooks.size());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен изменять книгу по id")
    @Test
    void update_whenCorrectRequest_thenUpdatedBook() {
        // input data
        Book updatableBook = dbBooks.get(0);
        Book expectedBook = new Book(updatableBook.getId(), "Updated Book Title", dbAuthors.get(0),
                List.of(dbGenres.get(3)));

        Optional<BookDto> existingBook = bookService.findById(expectedBook.getId());
        assertAll(
                () -> assertThat(existingBook).isPresent(),
                () -> assertThat(existingBook.get().getId()).isEqualTo(updatableBook.getId()),
                () -> assertThat(existingBook.get().getTitle()).isEqualTo(updatableBook.getTitle())
        );

        // testing
        BookDto updatedBook = bookService.update(updatableBook.getId(), expectedBook.getTitle(),
                expectedBook.getAuthor().getId(), Set.of(dbGenres.get(3).getId()));

        assertNotNull(updatedBook);

        assertThat(updatedBook)
                .usingRecursiveComparison()
                .ignoringFields("author")
                .isEqualTo(expectedBook);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен бросать исключение при обновлении, когда при обновлении книги передаваемый id жанра не найден")
    @Test
    void update_whenOneOfGenreIdIsNotExist_thenEntityNotFoundException() {
        // input data
        Book updatableBook = dbBooks.get(0);
        String title = "new title book";
        Long authorId = updatableBook.getAuthor().getId();
        Set<Long> genresIds = Set.of(updatableBook.getGenres().get(0).getId(), 100L);

        // testing
        assertThrows(EntityNotFoundException.class,
                () -> bookService.update(updatableBook.getId(), title, authorId, genresIds),
                "One or all genres with ids = '100L' not found");

        // verification
        var allBooks = bookService.findAll(PageRequest.of(0, 10));
        assertThat(allBooks).hasSize(dbBooks.size());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @DisplayName("должен удалять книгу по id")
    @Test
    void deleteById_whenBookExist_thenBookWasDeleted() {
        // input data
        Book deltableBook = dbBooks.get(0);

        Optional<BookDto> existingBook = bookService.findById(deltableBook.getId());
        assertAll(
                () -> assertThat(existingBook).isPresent(),
                () -> assertThat(existingBook.get().getId()).isEqualTo(deltableBook.getId()),
                () -> assertThat(existingBook.get().getTitle()).isEqualTo(deltableBook.getTitle())
        );

        // testing
        bookService.deleteById(deltableBook.getId());

        // verification
        Optional<BookDto> deletedBook = bookService.findById(deltableBook.getId());
        assertThat(deletedBook).isNotPresent();
    }
}