package ru.otus.hw.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookShortDto {

    private String id;

    private String title;

    private String authorId;

    private List<String> genres;
}