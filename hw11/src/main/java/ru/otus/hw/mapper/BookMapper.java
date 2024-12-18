package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.book.BookFullDto;
import ru.otus.hw.dto.book.BookShortDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {


    @Mapping(target = "genres", expression = "java(toGenresId(book.getGenres()))")
    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookShortDto toBookShortDto(Book book);

    default List<String> toGenresId(List<Genre> genres) {
        return genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }

    BookFullDto toBookFullDto(Book book);
}