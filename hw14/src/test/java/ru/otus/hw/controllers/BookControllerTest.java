package ru.otus.hw.controllers;

import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateRequest;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер по обработке endpoints с /books должен ")
@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookMapper bookMapper;

    private Faker faker = new Faker();

    static Stream<Arguments> userAuthProvider() {
        return Stream.of(
                Arguments.of("admin", "ROLE_ADMIN"),
                Arguments.of("user", "ROLE_USER")
        );
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать страницу со всеми книгами")
    void getAllBooks(String username, String role) throws Exception {
        BookDto bookDto = new BookDto(1L, faker.book().title(), new AuthorDto(1L, faker.name().fullName()),
                List.of(new GenreDto(1L, "Genre_1")));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(bookService.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(bookDto)));

        mockMvc.perform(get("/books/all")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(view().name("/books/get-all"));
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать страницу для редактирования книги")
    void updateBook(String username, String role) throws Exception {
        BookDto bookDto = new BookDto(1L, faker.book().title(), new AuthorDto(1L, faker.name().fullName()),
                List.of(new GenreDto(1L, "Genre_1")));

        when(bookService.findById(1L))
                .thenReturn(Optional.of(bookDto));
        when(authorService.findAll())
                .thenReturn(List.of(new AuthorDto(1L, "Author Name")));
        when(genreService.findAll())
                .thenReturn(List.of(new GenreDto(1L, "Genre Name")));
        when(bookMapper.toUpdateBookRequest(any()))
                .thenReturn(new BookUpdateRequest(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthor().getId(),
                        bookDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toList())));

        mockMvc.perform(get("/books/edit/1")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk())
                .andExpect(view().name("/books/edit-form"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать стартовую страницу при отправлении запроса на обновление книги ")
    void getUpdateBookPage(String username, String role) throws Exception {
        when(bookService.update(anyLong(), anyString(), anyLong(), anySet())).thenReturn(null);
        mockMvc.perform(post("/books/edit")
                        .param("id", "1")
                        .param("title", "Updated Title")
                        .param("authorId", "1")
                        .param("genreIds", "1", "2")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).update(eq(1L), eq("Updated Title"), eq(1L), anySet());
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать стартовую страницу при отправлении запроса на удаление книги")
    void deleteBook(String username, String role) throws Exception {
        doNothing()
                .when(bookService).deleteById(1L);

        mockMvc.perform(post("/books/delete/1")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).deleteById(1L);
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать страницу для создания книги")
    void getCreateBookPage(String username, String role) throws Exception {
        when(authorService.findAll())
                .thenReturn(List.of(new AuthorDto(1L, "Author Name")));
        when(genreService.findAll())
                .thenReturn(List.of(new GenreDto(1L, "Genre Name")));

        mockMvc.perform(get("/books/create")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk())
                .andExpect(view().name("/books/create-form"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать стартовую страницу при отправлении запроса на создание книги")
    void createBook(String username, String role) throws Exception {
        BookDto bookDto = new BookDto(1L, faker.book().title(), new AuthorDto(1L, faker.name().fullName()),
                List.of(new GenreDto(1L, "Genre_1")));

        when(bookService.save(anyString(), anyLong(), anySet()))
                .thenReturn(bookDto);

        mockMvc.perform(post("/books/create")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role)))
                        .param("title", "New Book")
                        .param("authorId", "1")
                        .param("genreIds", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService).save(eq("New Book"), eq(1L), anySet());
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать страницу с результатом поиска книги по id")
    void getBookById(String username, String role) throws Exception {
        BookDto bookDto = new BookDto(1L, faker.book().title(), new AuthorDto(1L, faker.name().fullName()),
                List.of(new GenreDto(1L, "Genre_1")));
        when(bookService.findById(anyLong()))
                .thenReturn(Optional.of(bookDto));

        mockMvc.perform(get("/books/find?bookId=".concat(bookDto.getId().toString()))
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("/books/get-book"));
    }

    @ParameterizedTest
    @MethodSource("userAuthProvider")
    @DisplayName(" возвращать страницу с ошибкой")
    void getBookByNotExistId(String username, String role) throws Exception {
        when(bookService.findById(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/books/find?bookId=100")
                        .with(user(username).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("index"));
    }
}