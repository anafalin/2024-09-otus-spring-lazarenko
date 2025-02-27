package ru.otus.hw.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateUpdateRequest {
    @NotEmpty(message = "Text cannot be null or empty")
    private String text;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;
}