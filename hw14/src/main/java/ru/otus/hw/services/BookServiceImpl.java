package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toBookDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findAll(Pageable paging) {
        Page<Book> booksPage = bookRepository.findAll(paging);
        return new PageImpl<>(bookMapper.toBookDtos(booksPage.getContent()));
    }

    @Override
    @Transactional
    public BookDto save(String title, Long authorId, Set<Long> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    @Transactional
    public BookDto update(Long id, String title, Long authorId, Set<Long> genresIds) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return save(id, title, authorId, genresIds);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));

        commentRepository.deleteAllByBookId(id);
        bookRepository.deleteById(id);
    }

    private BookDto save(Long id, String title, long authorId, Set<Long> genresIds) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id='%s' not found".formatted(authorId)));

        var genres = genreRepository.findAllByIdIn(genresIds);

        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids = '%s' not found".formatted(genresIds));
        }

        var book = new Book(id, title, author, genres);
        book = bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }
}