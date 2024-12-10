package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.author.AuthorDtoResponse;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookDtoResponse;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreDtoResponse;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookController.class})
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BookService bookService;

    private Faker faker = new Faker();

    @Test
    @DisplayName("должен возвращать корректный список всех книг")
    void shouldReturnCorrectBooksList() throws Exception {
        List<BookDto> bookDtoList = getBookDtoList();
        List<BookDtoResponse> bookDtoResponseList = getBookDtoResponseList(bookDtoList);

        given(bookService.findAll(PageRequest.of(0, 10))).willReturn(new PageImpl<>(bookDtoList));
        when(bookMapper.toBookDtoResponseList(anyList()))
                .thenReturn(bookDtoResponseList);

        mvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDtoList)));
    }

    @Test
    @DisplayName("должен возвращать корректную книгу по id")
    void shouldReturnCorrectBookById() throws Exception {
        List<BookDto> bookDtoList = getBookDtoList();
        List<BookDtoResponse> bookDtoResponseList = getBookDtoResponseList(bookDtoList);

        given(bookService.findById(bookDtoList.get(0).getId()))
                .willReturn(bookDtoList.get(0));
        when(bookMapper.toBookDtoResponse(bookDtoList.get(0))).thenReturn(bookDtoResponseList.get(0));

        mvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookDtoResponseList.get(0))));
    }

    @Test
    @DisplayName("должен возвращать ErrorResponse, если книги с запрашиваемым id не существует")
    void shouldReturnBadRequestIfBookNotExist() throws Exception {
        when(bookService.findById(anyLong()))
                .thenThrow(EntityNotFoundException.class);

        mvc.perform(get("/api/v1/books/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Должен корректно удалять книгу")
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(1L);
    }


    private List<BookDtoResponse> getBookDtoResponseList(List<BookDto> bookDtoList) {
        return bookDtoList.stream()
                .map(bookDto -> new BookDtoResponse(bookDto.getId(), bookDto.getTitle(),
                        new AuthorDtoResponse(bookDto.getAuthor().getId(), bookDto.getAuthor().getFullName()),
                        bookDto.getGenres().stream()
                                .map(genreDto -> new GenreDtoResponse(genreDto.getId(), genreDto.getName()))
                                .toList()))
                .toList();
    }

    private List<BookDto> getBookDtoList() {
        return List.of(
                new BookDto(
                        1L,
                        faker.book().title(),
                        new AuthorDto(1L, faker.book().author()),
                        List.of(new GenreDto(1L, faker.book().genre())))
        );
    }
}