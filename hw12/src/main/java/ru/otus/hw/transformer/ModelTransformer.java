package ru.otus.hw.transformer;

import ru.otus.hw.model.jpa.JpaAuthor;
import ru.otus.hw.model.jpa.JpaBook;
import ru.otus.hw.model.jpa.JpaComment;
import ru.otus.hw.model.jpa.JpaGenre;
import ru.otus.hw.model.mongo.MongoAuthor;
import ru.otus.hw.model.mongo.MongoBook;
import ru.otus.hw.model.mongo.MongoComment;
import ru.otus.hw.model.mongo.MongoGenre;

public class ModelTransformer {
    public static MongoAuthor authorTransform(JpaAuthor author) {
        return MongoAuthor.builder()
                .id(String.valueOf(author.getId()))
                .fullName(author.getFullName())
                .build();
    }

    public static MongoGenre genreTransform(JpaGenre genre) {
        return MongoGenre.builder()
                .id(String.valueOf(genre.getId()))
                .name(genre.getName())
                .build();
    }

    public static MongoBook bookTransform(JpaBook book) {
        return MongoBook.builder()
                .id(String.valueOf(book.getId()))
                .title(book.getTitle())
                .author(MongoAuthor.builder().id(String.valueOf(book.getAuthor().getId())).build())
                .genres(book.getGenres().stream().map(jpaGenre ->
                        MongoGenre.builder().id(String.valueOf(jpaGenre.getId())).build())
                        .toList())
                .build();
    }

    public static MongoComment commentTransform(JpaComment comment) {
        return MongoComment.builder()
                .id(String.valueOf(comment.getId()))
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .book(MongoBook.builder().id(String.valueOf(comment.getBook().getId())).build())
                .build();
    }
}