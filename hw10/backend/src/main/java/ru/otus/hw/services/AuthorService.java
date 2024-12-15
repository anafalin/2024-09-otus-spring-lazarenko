package ru.otus.hw.services;

import ru.otus.hw.dto.author.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();
}