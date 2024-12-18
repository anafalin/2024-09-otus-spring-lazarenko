package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.author.AuthorDto;
import ru.otus.hw.models.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDto toAuthorDto(Author author);
}