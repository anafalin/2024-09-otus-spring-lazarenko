package ru.otus.hw.testObjects;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class GeneratorData {

    public static List<Author> getDbAuthors() {
        return LongStream.range(1, 4)
                .boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1, 7)
                .boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return LongStream.range(1L, 4L)
                .boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id.intValue() - 1),
                        dbGenres.subList((id.intValue() - 1) * 2, (id.intValue() - 1) * 2 + 2)
                ))
                .toList();
    }

    public static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static Map<Long, List<Comment>> getDbMapCommentsByIdBook() {
        var dbBooks = GeneratorData.getDbBooks();
        return LongStream.range(1, 3)
                .boxed()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> List.of(new Comment(id,
                                "Comment to book_" + id,
                                LocalDate.of(2024, 11, 17),
                                dbBooks.get(id.intValue())))
                ));
    }

    public static List<Comment> getDbComments() {
        var dbBooks = GeneratorData.getDbBooks();
        return LongStream.range(1, 3)
                .boxed()
                .map(id -> new Comment(id,
                        "Comment to book_" + id,
                        LocalDate.of(2024, 11, 17),
                        dbBooks.get(id.intValue())))
                .toList();
    }
}