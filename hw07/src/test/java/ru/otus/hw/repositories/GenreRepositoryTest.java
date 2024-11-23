package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами ")
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = GeneratorData.getDbGenres();
    }

    @DisplayName("должен возвращать не пустой список жанров по указанным id")
    @Test
    void findAllByIds_whenGenresExist_thenNotEmptyGenreList() {
        var actualGenres = genreRepository.findAllByIdIn(Set.of(1L, 3L));
        var expectedGenres = List.of(dbGenres.get(0), dbGenres.get(2));

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        assertThat(actualGenres).hasSize(2);
    }

    @DisplayName("должен возвращать пустой список жанров по указанным id")
    @Test
    void findAllByIds_whenGenresNotExist_thenEmptyGenreList() {
        var actualGenres = genreRepository.findAllByIdIn(Set.of(100L));

        assertThat(actualGenres).isEmpty();
    }
}