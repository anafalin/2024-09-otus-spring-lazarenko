package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.UpdateBookRequest;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {
    BookDto toBookDto(Book book);

    List<BookDto> toBookDtos(List<Book> books);

    @Mapping(target = "genreIds", expression = "java(toGenreIds(book.getGenres()))")
    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    UpdateBookRequest toUpdateBookRequest(BookDto book);

    // Метод для конвертации List<Genre> в List<Long>
    default List<Long> toGenreIds(List<GenreDto> genres) {
        return genres.stream()
                .map(GenreDto::getId)
                .collect(Collectors.toList());
    }
}