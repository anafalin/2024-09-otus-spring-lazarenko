package ru.otus.hw.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateUpdateResponse {
    private Long id;

    private String text;

    private Long bookId;

    private LocalDate createdAt;
}