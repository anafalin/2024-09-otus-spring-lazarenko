package ru.otus.hw;

import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.book.BookFullDto;
import ru.otus.hw.dto.book.BookShortDto;
import ru.otus.hw.dto.comment.CommentDto;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class TestDataGenerator {

    public static List<GenreDto> getGenreDtos() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new GenreDto(id.toString(), "Genre_" + id))
                .toList();
    }

    public static List<BookShortDto> getBookShortDtos() {
        List<AuthorDto> dbAuthors = IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id.toString(), "Author_" + id))
                .toList();
        var dbGenres = getGenreDtos();
        return getBookShortDtos(dbAuthors, dbGenres);
    }

    public static List<BookShortDto> getBookShortDtos(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookShortDto(id.toString(), "BookTitle_" + id,
                        dbAuthors.get(id - 1).getId(), List.of(dbGenres.get(id - 1).getId())))
                .toList();
    }

    public static List<BookFullDto> getBookFullDtos(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookFullDto(id.toString(), "BookTitle_" + id,
                        dbAuthors.get(id - 1), List.of(dbGenres.get(id - 1))))
                .toList();
    }


    public static List<BookFullDto> getBookFullDtos() {
        List<AuthorDto> dbAuthors = IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id.toString(), "Author_" + id))
                .toList();
        var dbGenres = getGenreDtos();
        return getBookFullDtos(dbAuthors, dbGenres);
    }

    public static List<CommentDto> getCommentDtos(List<BookFullDto> dbBooks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new CommentDto(id.toString(), "Message_" + id,
                        dbBooks.get(id - 1).getId(), LocalDate.of(2024, 12, 10)))
                .toList();
    }

    public static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id.toString(), "Author_" + id))
                .toList();
    }

    public static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id.toString(), "Genre_" + id))
                .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id.toString(), "BookTitle_" + id,
                        dbAuthors.get(id - 1), List.of(dbGenres.get(id - 1))))
                .toList();
    }

    public static List<Book> getDbBooks() {
        List<Author> dbAuthors = IntStream.range(1, 4).boxed()
                .map(id -> new Author(id.toString(), "Author_" + id))
                .toList();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    public static List<Comment> getDbComments() {
        List<Book> dbBooks = getDbBooks();
        return getDbComments(dbBooks);
    }

    public static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(id.toString(), "Message_" + id,
                        LocalDate.of(2024, 12, 10), dbBooks.get(id - 1)))
                .toList();
    }


}
