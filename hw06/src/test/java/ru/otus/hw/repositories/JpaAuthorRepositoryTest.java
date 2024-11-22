package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.testObjects.GeneratorData;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
@Import({JpaAuthorRepository.class})
class JpaAuthorRepositoryTest {

    @Autowired
    JpaAuthorRepository authorRepository;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = GeneratorData.getDbAuthors();
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void findAll_returnCorrectNotEmptyGenresList_genresExist() {
        var actualAuthors = authorRepository.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен возвращать автора по id")
    @Test
    void findById_correctAuthor_authorExist() {
        var actualAuthor = authorRepository.findById(2);
        var expectedAuthor = dbAuthors.get(1);

        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен возвращать empty optional когда автор с указанным id не существует")
    @Test
    void findById_emptyOptional_authorNotExist() {
        var result = authorRepository.findById(100);

        assertThat(result).isEmpty();
    }
}