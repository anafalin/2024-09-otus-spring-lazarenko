package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с жанрами ")
@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private List<Genre> dbGenres;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        dbGenres = GeneratorData.getDbGenres();
    }

    @DisplayName("должен возвращать не пустой список жанров по указанным id")
    @Test
    @Order(1)
    void findAllByIds_whenGenresExist_thenNotEmptyGenreList() {
        var actualGenres = genreRepository.findAllByIdIn(Set.of("1", "3"));
        var expectedGenres = List.of(dbGenres.get(0), dbGenres.get(2));

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        assertThat(actualGenres).hasSize(2);
    }

    @DisplayName("должен возвращать пустой список жанров по указанным id")
    @Test
    @Order(2)
    void findAllByIds_whenGenresNotExist_thenEmptyGenreList() {
        var actualGenres = genreRepository.findAllByIdIn(Set.of("100"));

        assertThat(actualGenres).isEmpty();
    }
}