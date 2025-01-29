package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@DisplayName("Контроллер по обработке endpoints с /books должен ")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    @DisplayName(" возвращать статус 403 при попытке отредактировать книгу пользователем, не имеющим разрешение")
    void getUpdateBookPage_thenForbidden_userHasNotPermission() throws Exception {
        mockMvc.perform(get("/books/edit/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "supervisor", roles = "EDITOR")
    @DisplayName(" возвращать статус 200 при попытке отредактировать книгу пользователем, имеющим разрешение")
    void getUpdateBookPage_thenOk_userIsEditorAndHasPermission() throws Exception {
        mockMvc.perform(get("/books/edit/3"))
                .andExpect(status().isOk())
                .andExpect(view().name("/books/edit-form"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    @DisplayName(" возвращать статус 200 при попытке отредактировать книгу пользователем, имеющим разрешение")
    void getUpdateBookPage_thenOk_userIsUserAndHasPermission() throws Exception {
        mockMvc.perform(get("/books/edit/3"))
                .andExpect(status().isOk())
                .andExpect(view().name("/books/edit-form"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    @DisplayName(" возвращать статус 403 при попытке отправить post запрос на редактирование книгни пользователем, " +
            "не имеющим разрешение")
    void updateBook_thenForbidden_userHasNotPermission() throws Exception {

        mockMvc.perform(post("/books/edit")
                        .param("id", "2")
                        .param("title", "Updated Title")
                        .param("authorId", "1")
                        .param("genreIds", "1", "2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    @DisplayName(" выполнять редирект при попытке отправить post запрос на редактирование книгни пользователем, " +
            "имеющим разрешение")
    void updateBook_thenOkAndRedirect_userHasPermission() throws Exception {
        mockMvc.perform(post("/books/edit")
                        .param("id", "3")
                        .param("title", "Updated Title")
                        .param("authorId", "1")
                        .param("genreIds", "1", "2"))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    @DisplayName(" возвращать статус 403 при попытке отправить post запрос на удаление книги пользователем, " +
            "не имеющим разрешение")
    void deleteBook_thenForbidden_userHasNotPermission() throws Exception {
        mockMvc.perform(post("/books/delete/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    @DisplayName(" возвращать статус 403 при попытке отправить post запрос на удаление книги пользователем, " +
            "имеющим разрешение")
    void deleteBook_thenOkAndRedirect_userHasPermission() throws Exception {
        mockMvc.perform(post("/books/delete/3"))
                .andExpect(redirectedUrl("/"));
    }
}