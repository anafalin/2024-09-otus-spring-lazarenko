package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        Book book = jdbc.query("""
                        select
                        b.id as id,
                        b.title as title,
                        b.author_id as author_id,
                        a.full_name as author_full_name,
                        g.id as genre_id,
                        g.name as genre_name,
                        from books b
                        left join authors a on b.author_id = a.id
                        left join books_genres bg on b.id = bg.book_id
                        left join genres g on g.id = bg.genre_id
                        where b.id = :id""",
                params, new BookResultSetExtractor());
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("""
                        select
                         b.id as id,
                         b.title as title,
                         b.author_id as author_id,
                         a.full_name as author_full_name,
                         g.id as genre_id,
                         g.name as genre_name,
                         from books b
                         left join authors a on b.author_id = a.id
                         left join books_genres bg on b.id = bg.book_id
                         left join genres g on g.id = bg.genre_id""",
                new BooksResultExtractor());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId()));

        jdbc.update("insert into books (title, author_id) values (:title, :author_id)", params, keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        int countAffected = jdbc.update("update books set title = :title, author_id = :author_id where id = :id",
                Map.of("title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "id", book.getId()));

        if (countAffected == 0) {
            throw new EntityNotFoundException("Book with id = '%s' not exist".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var relations = book.getGenres().stream()
                .map(genre -> new BookGenreRelation(book.getId(), genre.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
        var params = SqlParameterSourceUtils.createBatch(relations);

        jdbc.batchUpdate("insert into books_genres (book_id, genre_id) values (:bookId, :genreId)",
                params);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbc.update("delete from books_genres where book_id = :bookId ",
                Map.of("bookId", book.getId()));
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {
        private final Book book = new Book();

        private final Author author = new Author();

        private final Map<Long, Genre> mapGenres = new HashMap<>();

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }

            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));

            author.setId(rs.getLong("author_id"));
            author.setFullName(rs.getString("author_full_name"));

            do {
                extractedGenre(rs);
            } while (rs.next());

            book.setAuthor(author);
            book.setGenres(mapGenres.values().stream().toList());
            return book;
        }

        private void extractedGenre(ResultSet rs) throws SQLException {
            var genreId = rs.getLong("genre_id");
            if (!mapGenres.containsKey(genreId)) {
                var genreName = rs.getString("genre_name");
                mapGenres.put(genreId, new Genre(genreId, genreName));
            }
        }
    }

    private static class BooksResultExtractor implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            var booksByIdMap = new HashMap<Long, Book>();
            while (rs.next()) {
                var bookId = rs.getLong("id");
                booksByIdMap.putIfAbsent(bookId, extractBook(rs));
                booksByIdMap.get(bookId).getGenres().add(extractGenre(rs));
            }
            return booksByIdMap.values().stream().toList();
        }

        private Book extractBook(ResultSet rs) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setGenres(new ArrayList<>());
            book.setAuthor(extractAuthor(rs));
            return book;
        }

        private Author extractAuthor(ResultSet rs) throws SQLException {
            return new Author(
                    rs.getLong("author_id"),
                    rs.getString("author_full_name"));
        }

        private Genre extractGenre(ResultSet rs) throws SQLException {
            var genreId = rs.getLong("genre_id");
            var genreName = rs.getString("genre_name");
            return new Genre(genreId, genreName);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}