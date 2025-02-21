package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreDtoResponse;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.services.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({GenreController.class})
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreMapper genreMapper;

    private Faker faker = new Faker();

    @Test
    @DisplayName("должен возвращать список всех жанров")
    void shouldReturnCorrectGenresList() throws Exception {
        List<GenreDto> genreDtoList = List.of(
                new GenreDto(1L, faker.book().genre()),
                new GenreDto(2L, faker.book().genre())
        );

        List<GenreDtoResponse> genreDtoResponses = genreDtoList.stream()
                .map(genreDto -> new GenreDtoResponse(genreDto.getId(), genreDto.getName()))
                .toList();

        given(genreService.findAll()).willReturn(genreDtoList);
        when(genreMapper.toGenreDtoResponseList(anyList()))
                .thenReturn(genreDtoResponses);

        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreDtoList)));
    }
}