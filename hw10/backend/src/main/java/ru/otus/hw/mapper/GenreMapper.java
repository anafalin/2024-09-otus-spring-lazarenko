package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.genre.GenreDtoResponse;
import ru.otus.hw.models.Genre;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toGenreDto(Genre genre);

    List<GenreDto> toGenreDtos(List<Genre> genres);

    List<GenreDtoResponse> toGenreDtoResponseList(List<GenreDto> all);
}