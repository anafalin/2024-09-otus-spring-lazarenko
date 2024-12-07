package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreDto toGenreDto(Genre genre);

    List<GenreDto> toGenreDtos(List<Genre> genres);
}