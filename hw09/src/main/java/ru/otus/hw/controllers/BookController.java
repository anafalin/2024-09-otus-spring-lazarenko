package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CreateUpdateBookRequest;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.UpdateBookRequest;
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

        List<BookDto> books = pageBooks.getContent();

        model.addAttribute("books", books);
        model.addAttribute("currentPage", pageBooks.getNumber() + 1);
        model.addAttribute("totalItems", pageBooks.getTotalElements());
        model.addAttribute("totalPages", pageBooks.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/books/get-all";
    }

    @GetMapping("/create")
    public String getCreateBookPage(Model model) {
        model.addAttribute("book", new CreateUpdateBookRequest());

        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);

        return "/books/create-form";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute CreateUpdateBookRequest request) {
        bookService.save(request.getTitle(), request.getAuthorId(),
                request.getGenresIds().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        return "redirect:/";
    }


    @GetMapping("/find")
    public String getBookById(@RequestParam(name = "bookId") Long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("book", book);
        return "/books/get-book";
    }

    @GetMapping("/edit/{id}")
    public String updateBook(@PathVariable("id") long id, Model model) {
        BookDto book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        model.addAttribute("book", bookMapper.toUpdateBookRequest(book));
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "/books/edit-form";
    }

    @PostMapping("/edit")
    public String getUpdateBookPage(@ModelAttribute("book") UpdateBookRequest request) {
        bookService.update(request.getId(), request.getTitle(), request.getAuthorId(),
                request.getGenresIds().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}