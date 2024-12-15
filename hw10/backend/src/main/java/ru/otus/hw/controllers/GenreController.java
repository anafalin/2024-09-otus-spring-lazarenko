package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.genre.GenreDtoResponse;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    private final GenreMapper genreMapper;

    @GetMapping
    public List<GenreDtoResponse> getAllGenres() {
        return genreMapper.toGenreDtoResponseList(genreService.findAll());
    }
}