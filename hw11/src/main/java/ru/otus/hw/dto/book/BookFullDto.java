package ru.otus.hw.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.author.AuthorDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFullDto {

    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}