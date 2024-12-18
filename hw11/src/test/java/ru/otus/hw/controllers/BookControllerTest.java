package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.TestDataGenerator;
import ru.otus.hw.dto.book.BookCreateRequest;
import ru.otus.hw.dto.book.BookFullDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static ru.otus.hw.TestDataGenerator.getBookFullDtos;
import static ru.otus.hw.TestDataGenerator.getDbAuthors;
import static ru.otus.hw.TestDataGenerator.getDbBooks;
import static ru.otus.hw.TestDataGenerator.getDbGenres;


@SpringBootTest(classes = {BookController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({BookRepository.class, BookMapper.class, AuthorRepository.class, GenreRepository.class})
@EnableAutoConfiguration
public class BookControllerTest {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookMapper bookMapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    void getAll_notEmptyResult_booksExist() {
        Flux<Book> booksFlux = Flux.fromIterable(getDbBooks());

        when(bookRepository.findAll()).thenReturn(booksFlux);
        when(bookMapper.toBookFullDto(getDbBooks().get(0)))
                .thenReturn(getBookFullDtos().get(0));
        when(bookMapper.toBookFullDto(getDbBooks().get(1)))
                .thenReturn(getBookFullDtos().get(1));
        when(bookMapper.toBookFullDto(getDbBooks().get(2)))
                .thenReturn(getBookFullDtos().get(2));

        webClient.get().uri("/reactive/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookFullDto.class);
    }

    @Test
    void getAll_emptyResult_booksNotExist() {
        Flux<Book> booksFlux = Flux.empty();

        when(bookRepository.findAll()).thenReturn(booksFlux);

        webClient.get().uri("/reactive/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookFullDto.class);
    }

    @Test
    public void getById_correctBook_bookExists() {
        Mono<Book> booksMono = Mono.just(getDbBooks().get(0));
        BookFullDto expectedBookDto = getBookFullDtos().get(0);

        when(bookRepository.findById(expectedBookDto.getId()))
                .thenReturn(booksMono);
        when(bookMapper.toBookFullDto(getDbBooks().get(0)))
                .thenReturn(expectedBookDto);

        WebTestClient.ResponseSpec response = webClient.get().uri("/reactive/books/" + expectedBookDto.getId())
                .exchange()
                .expectStatus()
                .isOk();

        response.expectBody()
                .jsonPath("$.title").isEqualTo(expectedBookDto.getTitle())
                .jsonPath("$.author.id").isEqualTo(expectedBookDto.getAuthor().getId())
                .jsonPath("$.author.fullName").isEqualTo(expectedBookDto.getAuthor().getFullName())
                .jsonPath("$.genres").isNotEmpty()
                .jsonPath("$.genres.[0].name").isEqualTo(expectedBookDto.getGenres().get(0).getName());
    }

    @Test
    public void getById_statusNotFound_bookNotExist() {
        String incorrectId = "1000";

        when(bookRepository.findById(anyString()))
                .thenThrow(EntityNotFoundException.class);

        webClient.get().uri("/reactive/books/" + incorrectId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void updateTest_statusOk_bookExists() {
        String id = "1";
        BookCreateRequest request = new BookCreateRequest("BookTitle_1", "1", List.of("1"));

        Mono<Book> bookMono = Mono.just(getDbBooks().get(0));
        when(bookRepository.findById(anyString())).thenReturn(bookMono);
        when(genreRepository.findAllByIdIn(anyList())).thenReturn(Flux.just(getDbGenres().get(0)));
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getDbAuthors().get(0)));
        when(bookRepository.save(any())).thenReturn(bookMono);
        when(bookMapper.toBookShortDto(getDbBooks().get(0))).thenReturn(TestDataGenerator.getBookShortDtos().get(0));

        WebTestClient.ResponseSpec response = webClient.put().uri("/reactive/books/" + id)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isOk();

        System.out.println(response);
        response.expectBody()
                .jsonPath("$.title").isEqualTo(request.getTitle())
                .jsonPath("$.genres").isNotEmpty()
                .jsonPath("$.authorId").isEqualTo(request.getAuthorId())
                .jsonPath("$.genres").isNotEmpty()
                .jsonPath("$.genres.[0]").isEqualTo(request.getGenreIds().get(0));
    }

    @Test
    public void updateTest_statusNotFound_bookNotExists() {
        String incorrectId = "100";
        BookCreateRequest request = new BookCreateRequest("BookTitle_1", "1", List.of("1"));

        when(genreRepository.findAllByIdIn(anyList()))
                .thenReturn(Flux.just(getDbGenres().get(0)));
        when(authorRepository.findById(anyString()))
                .thenReturn(Mono.just(getDbAuthors().get(0)));
        when(bookRepository.findById(anyString()))
                .thenReturn(Mono.empty());

        webClient.put().uri("/reactive/books/" + incorrectId)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void create_statusCreated_requestIsCorrect() {
        BookCreateRequest request = new BookCreateRequest("BookTitle_1", "1", List.of("1"));

        Mono<Book> bookMono = Mono.just(getDbBooks().get(0));
        when(bookRepository.findById(anyString())).thenReturn(bookMono);
        when(genreRepository.findAllByIdIn(anyList())).thenReturn(Flux.just(getDbGenres().get(0)));
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(getDbAuthors().get(0)));
        when(bookRepository.save(any())).thenReturn(bookMono);
        when(bookMapper.toBookShortDto(getDbBooks().get(0))).thenReturn(TestDataGenerator.getBookShortDtos().get(0));

        WebTestClient.ResponseSpec response = webClient.post().uri("/reactive/books")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isCreated();

        response.expectBody()
                .jsonPath("$.title").isEqualTo(request.getTitle())
                .jsonPath("$.authorId").isEqualTo(request.getAuthorId());
    }

    @Test
    public void deleteTest() {
        String id = "1";
        webClient.delete().uri("/reactive/books/" + id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}