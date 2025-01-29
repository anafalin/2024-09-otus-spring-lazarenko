package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateRequest;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateRequest;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper;

    @GetMapping("/all")
    public String getAllBooks(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Pageable paging = PageRequest.of(page - 1, size);

        Page<BookDto> pageBooks = bookService.findAll(paging);

        model.addAttribute("books", pageBooks.getContent());
        model.addAttribute("currentPage", pageBooks.getNumber() + 1);
        model.addAttribute("totalItems", pageBooks.getTotalElements());
        model.addAttribute("totalPages", pageBooks.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/books/get-all";
    }

    @GetMapping("/create")
    public String getCreateBookPage(Model model) {
        model.addAttribute("book", new BookCreateRequest());

        addAuthorsAndGenresInModel(model);

        return "/books/create-form";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute BookCreateRequest request, Model model) {
        if (request.getGenreIds().isEmpty()) {
            model.addAttribute("book", request);

            addAuthorsAndGenresInModel(model);
            model.addAttribute("error", "Жанры не могут быть пустыми");
            return "/books/create-form";
        }

        bookService.save(request.getTitle(), request.getAuthorId(),
                request.getGenreIds().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        return "redirect:/";
    }

    @GetMapping("/find")
    public String getBookById(@RequestParam(name = "bookId") Long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        return "/books/get-book";
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.BookDto', 'WRITE')")
    @GetMapping("/edit/{id}")
    public String getUpdateBookPage(@PathVariable("id") long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        model.addAttribute("book", bookMapper.toUpdateBookRequest(book));
        addAuthorsAndGenresInModel(model);
        return "/books/edit-form";
    }

    @PreAuthorize("hasPermission(#request.id, 'ru.otus.hw.dto.BookDto', 'WRITE')")
    @PostMapping("/edit")
    public String updateBook(@ModelAttribute("book") BookUpdateRequest request) {
        bookService.update(request.getId(), request.getTitle(), request.getAuthorId(),
                request.getGenreIds().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        return "redirect:/";
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.BookDto', 'WRITE')")
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/owners/userId/manage")
    public String getAllBooksByOwner(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "50") int size,
                                     Model model) {
        Pageable paging = PageRequest.of(page - 1, size);

        List<BookDto> books = bookService.findAllOwnersBook(paging);
        model.addAttribute("books", books);
        return "/books/get-owner-books";
    }

    private void addAuthorsAndGenresInModel(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
    }
}