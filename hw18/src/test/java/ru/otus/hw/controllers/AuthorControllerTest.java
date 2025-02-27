package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.author.AuthorDtoResponse;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.services.AuthorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthorController.class})
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private AuthorMapper authorMapper;

    private Faker faker = new Faker();

    @Test
    @DisplayName("должен возвращать корректный список всех авторов")
    void shouldReturnCorrectAuthorsList() throws Exception {
        List<AuthorDto> authorDtoList = List.of(
                new AuthorDto(1L, faker.name().fullName())
        );

        List<AuthorDtoResponse> authorDtoResponseList = authorDtoList.stream()
                .map(a -> mapper.convertValue(a, AuthorDtoResponse.class))
                .toList();

        given(authorService.findAll()).willReturn(authorDtoList);
        when(authorMapper.toAuthorDtoResponseList(anyList()))
                .thenReturn(authorDtoResponseList);

        mvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorDtoList)));
    }
}