package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import({JdbcGenreRepository.class})
class JdbcGenreRepositoryTest {

    @Autowired
    JdbcGenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll_correctNotEmptyGenresList_genresExist() {
        var actualBooks = genreRepository.findAll();
        var expectedBooks = dbGenres;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен загружать список жанров по указанными id")
    @Test
    void findAllByIds_correctNotEmptyGenresList_genresExist() {
        var actualBooks = genreRepository.findAllByIds(Set.of(1L, 3L));
        var expectedBooks = List.of(dbGenres.get(0), dbGenres.get(2));

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        assertThat(actualBooks).hasSize(2);
    }

    @DisplayName("должен возвращать список жанров по указанными id")
    @Test
    void findAllByIds_emptyGenresList_genresNotExist() {
        var actualBooks = genreRepository.findAllByIds(Set.of(100L));

        assertThat(actualBooks).isEmpty();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}