package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.book.BookCreateResponse;
import ru.otus.hw.dto.book.BookDto;
import ru.otus.hw.dto.book.BookDtoResponse;
import ru.otus.hw.dto.genre.GenreDto;
import ru.otus.hw.dto.book.BookUpdateRequest;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {
    BookDto toBookDto(Book book);

    List<BookDto> toBookDtos(List<Book> books);

    @Mapping(target = "genreIds", expression = "java(toGenreIds(book.getGenres()))")
    @Mapping(target = "authorId", expression = "java(book.getAuthor().getId())")
    BookUpdateRequest toUpdateBookRequest(BookDto book);

    BookCreateResponse toBookCreateResponse(BookDto savedBook);

    BookDtoResponse toBookDtoResponse(BookDto byId);

    List<BookDtoResponse> toBookDtoResponseList(List<BookDto> content);

    // Метод для конвертации List<Genre> в List<Long>
    default List<Long> toGenreIds(List<GenreDto> genres) {
        return genres.stream()
                .map(GenreDto::getId)
                .collect(Collectors.toList());
    }

}