package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = GeneratorData.getDbGenres();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void findAll_correctNotEmptyGenresList_genresExist() {
        var actualGenres = genreRepository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать не пустой список жанров по указанными id")
    @Test
    void findAllByIds_correctNotEmptyGenresList_genresExist() {
        var actualGenres = genreRepository.findAllByIds(Set.of(1L, 3L));
        var expectedGenres = List.of(dbGenres.get(0), dbGenres.get(2));

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        assertThat(actualGenres).hasSize(2);
    }

    @DisplayName("должен загружать пустой список жанров по указанными id")
    @Test
    void findAllByIds_emptyGenresList_genresNotExist() {
        var actualGenres = genreRepository.findAllByIds(Set.of(100L));

        assertThat(actualGenres).isEmpty();
    }
}