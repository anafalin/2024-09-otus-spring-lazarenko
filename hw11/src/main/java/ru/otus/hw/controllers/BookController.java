package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.book.BookCreateRequest;
import ru.otus.hw.dto.book.BookFullDto;
import ru.otus.hw.dto.book.BookShortDto;
import ru.otus.hw.dto.book.BookUpdateRequest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reactive/")
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @GetMapping(value = "/books", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookFullDto> getAll() {
        return bookRepository.findAll().map(bookMapper::toBookFullDto);
    }

    @GetMapping("/books/{id}")
    public Mono<BookFullDto> getById(@PathVariable("id") String id) {
        return findBookById(id)
                .map(bookMapper::toBookFullDto);
    }

    @PutMapping("/books/{id}")
    public Mono<BookShortDto> update(@PathVariable("id") String id,
                                     @RequestBody BookUpdateRequest request) {

        return Mono.zip(getAuthorById(request.getAuthorId()),
                        getGenresByIds(request.getGenreIds()))
                .flatMap(tuple -> findBookById(id)
                        .map(book -> {
                            book.setTitle(request.getTitle());
                            book.setAuthor(tuple.getT1());
                            book.setGenres(tuple.getT2());
                            return book;
                        }))
                .flatMap(bookRepository::save)
                .map(bookMapper::toBookShortDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/books")
    public Mono<BookShortDto> create(@RequestBody BookCreateRequest request) {
        return Mono.zip(getAuthorById(request.getAuthorId()),
                        getGenresByIds(request.getGenreIds()))
                .flatMap(tuple -> {
                    Book book = new Book(null, request.getTitle(), tuple.getT1(), tuple.getT2());
                    return bookRepository.save(book);
                })
                .map(bookMapper::toBookShortDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/books/{id}")
    public Mono<Void> delete(@PathVariable("id") String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> findBookById(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        "Book with id='%s' not found".formatted(id))));
    }

    private Mono<Author> getAuthorById(String id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        "Author with id='%s' not found".formatted(id))));
    }

    private Mono<List<Genre>> getGenresByIds(List<String> ids) {
        return genreRepository.findAllByIdIn(ids)
                .collectList()
                .switchIfEmpty(Mono.error(new EntityNotFoundException(
                        "One or more genre with id in '%s' not found".formatted(ids))));
    }
}