package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog
public class InitDataBaseChangeLog {
    private Map<Integer, Genre> genreMap = new HashMap<>();

    private Map<Integer, Author> authorMap = new HashMap<>();

    private Map<Integer, Book> bookMap = new HashMap<>();

    @ChangeSet(order = "001", id = "dropDb", author = "Anastasia Lazarenko", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "Anastasia.Lazarenko")
    public void insertAuthors(AuthorRepository authorRepository) {
        authorMap.put(1, new Author("1", "Author_1"));
        authorMap.put(2, new Author("2", "Author_2"));
        authorMap.put(3, new Author("3", "Author_3"));

        authorRepository.saveAll(authorMap.values()).blockLast();
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "Anastasia.Lazarenko")
    public void insertGenres(GenreRepository genreRepository) {
        genreMap.put(1, new Genre("1", "Genre_1"));
        genreMap.put(2, new Genre("2", "Genre_2"));
        genreMap.put(3, new Genre("3", "Genre_3"));
        genreMap.put(4, new Genre("4", "Genre_4"));
        genreMap.put(5, new Genre("5", "Genre_5"));
        genreMap.put(6, new Genre("6", "Genre_6"));

        genreRepository.saveAll(genreMap.values()).blockLast();
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "Anastasia.Lazarenko")
    public void insertBooks(BookRepository bookRepository) {
        bookMap.put(1, new Book("1", "BookTitle_1", authorMap.get(1),
                List.of(genreMap.get(1), genreMap.get(2))));
        bookMap.put(2, new Book("2", "BookTitle_2", authorMap.get(2),
                List.of(genreMap.get(3), genreMap.get(4))));
        bookMap.put(3, new Book("3", "BookTitle_3", authorMap.get(3),
                List.of(genreMap.get(5), genreMap.get(6))));

        bookRepository.saveAll(bookMap.values()).blockLast();
    }

    @ChangeSet(order = "005", id = "insertComments", author = "Anastasia.Lazarenko")
    public void insertComments(CommentRepository commentRepository) {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("1", "Comment_1 to Book_1",
                LocalDate.of(2024, 11, 01), bookMap.get(1)));
        comments.add(new Comment("2", "Comment_2 to Book_1",
                LocalDate.of(2024, 11, 02), bookMap.get(1)));

        comments.add(new Comment("3", "Comment_2 to Book_2",
                LocalDate.of(2024, 11, 02), bookMap.get(2)));
        comments.add(new Comment("4", "Comment_3 to Book_1",
                LocalDate.of(2024, 11, 03), bookMap.get(2)));

        commentRepository.saveAll(comments).blockLast();
    }
}