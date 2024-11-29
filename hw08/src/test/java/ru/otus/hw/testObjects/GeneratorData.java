package ru.otus.hw.testObjects;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class GeneratorData {

    public static List<Author> getDbAuthors() {
        return LongStream.range(1, 4)
                .boxed()
                .map(id -> new Author(Integer.toString(id.intValue()), "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return LongStream.range(1, 7)
                .boxed()
                .map(id -> new Genre(Integer.toString(id.intValue()), "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return LongStream.range(1L, 4L)
                .boxed()
                .map(id -> new Book(Integer.toString(id.intValue()),
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

    public static Map<String, List<Comment>> getDbMapCommentsByIdBook() {
        var dbBooks = GeneratorData.getDbBooks();
        Map<String, List<Comment>> map = new HashMap<>();
        map.put("1", LongStream.range(1, 3)
                .boxed()
                .map(id ->
                        new Comment(Integer.toString(id.intValue()),
                                "Comment_" + id + " to Book_" + 1,
                                LocalDate.of(2024, 11, id.intValue()),
                                dbBooks.get(0)))
                .toList());

        map.put("2", LongStream.range(3, 5)
                .boxed()
                .map(id ->
                        new Comment(Integer.toString(id.intValue()),
                                "Comment_" + (id - 3) + " to Book_" + 2,
                                LocalDate.of(2024, 11, id.intValue()),
                                dbBooks.get(1)))
                .toList());

        return map;
    }

    public static List<Comment> getDbComments() {
        var dbBooks = GeneratorData.getDbBooks();
        List<Comment> comments = new ArrayList<>();
        comments.addAll(LongStream.range(1, 3)
                .boxed()
                .map(id ->
                        new Comment(Integer.toString(id.intValue()),
                                "Comment_" + id + " to Book_" + 1,
                                LocalDate.of(2024, 11, id.intValue()),
                                dbBooks.get(0)))
                .toList());

        comments.addAll(LongStream.range(3, 5)
                .boxed()
                .map(id ->
                        new Comment(Integer.toString(id.intValue()),
                                "Comment_" + (id - 3) + " to Book_" + 2,
                                LocalDate.of(2024, 11, id.intValue()),
                                dbBooks.get(1)))
                .toList());

        return comments;
    }
}