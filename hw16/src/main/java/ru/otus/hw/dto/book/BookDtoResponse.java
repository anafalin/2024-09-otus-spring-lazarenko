package ru.otus.hw.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.author.AuthorDtoResponse;
import ru.otus.hw.dto.genre.GenreDtoResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoResponse {

    private Long id;

    private String title;

    private AuthorDtoResponse author;

    private List<GenreDtoResponse> genres;
}