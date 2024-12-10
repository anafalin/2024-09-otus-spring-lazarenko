package ru.otus.hw.dto.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateRequest {
    @NotEmpty(message = "Title cannot be null or empty")
    private String title;

    @NotNull(message = "authorId cannot be null")
    private Long authorId;

    @NotEmpty(message = "genreIds cannot be null or empty")
    private List<Long> genreIds = new ArrayList<>();
}