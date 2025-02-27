package ru.otus.hw.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.hw.dto.book.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable paging);

    BookDto save(String title, Long authorId, List<Long> genresIds);

    BookDto update(Long id, String title, Long authorId, List<Long> genresIds);

    void deleteById(Long id);

    Long getCountBooks();
}