package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<Book> findById(Long id);

    List<Book> findAll();

    Book insert(String title, Long authorId, Set<Long> genresIds);

    Book update(Long id, String title, Long authorId, Set<Long> genresIds);

    void deleteById(Long id);
}