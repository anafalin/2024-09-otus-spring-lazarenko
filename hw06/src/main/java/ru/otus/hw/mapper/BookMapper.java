package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {
    BookDto toBookDto(Book book);

    List<BookDto> toBookDtos(List<Book> books);
}