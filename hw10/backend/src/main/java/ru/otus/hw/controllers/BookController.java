package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.book.BookCreateRequest;
import ru.otus.hw.dto.book.BookCreateResponse;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookDtoResponse;
import ru.otus.hw.dto.book.BookUpdateRequest;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.services.BookService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping
    public List<BookDtoResponse> getAllBooks(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page - 1, size);

        Page<BookDto> pageBooks = bookService.findAll(paging);

        return bookMapper.toBookDtoResponseList(pageBooks.getContent());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookCreateResponse createBook(@RequestBody BookCreateRequest request) {
        BookDto savedBook = bookService.save(request.getTitle(), request.getAuthorId(),
                request.getGenreIds());
        return bookMapper.toBookCreateResponse(savedBook);
    }

    @GetMapping("/{id}")
    public BookDtoResponse getBookById(@PathVariable Long id) {
        return bookMapper.toBookDtoResponse(bookService.findById(id));
    }

    @PutMapping("/{id}")
    public BookDtoResponse updateBook(@RequestBody BookUpdateRequest request, @PathVariable Long id) {
        BookDto updatedBook = bookService.update(id, request.getTitle(), request.getAuthorId(),
                request.getGenreIds());
        return bookMapper.toBookDtoResponse(updatedBook);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }
}