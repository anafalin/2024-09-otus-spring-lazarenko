package ru.otus.hw.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateRequest {
    private String title;

    private String authorId;

    private List<String> genreIds = new ArrayList<>();
}