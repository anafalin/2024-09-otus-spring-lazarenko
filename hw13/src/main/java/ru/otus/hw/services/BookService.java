package ru.otus.hw.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.hw.dto.BookDto;

import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDto> findById(Long id);

    Page<BookDto> findAll(Pageable paging);

    BookDto save(String title, Long authorId, Set<Long> genresIds);

    BookDto update(Long id, String title, Long authorId, Set<Long> genresIds);

    void deleteById(Long id);
}