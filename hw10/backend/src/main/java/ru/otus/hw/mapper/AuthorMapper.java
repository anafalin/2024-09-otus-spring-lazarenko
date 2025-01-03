package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.dto.author.AuthorDtoResponse;
import ru.otus.hw.models.Author;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toAuthorDto(Author author);

    List<AuthorDto> toAuthorDtos(List<Author> authors);

    List<AuthorDtoResponse> toAuthorDtoResponseList(List<AuthorDto> all);
}