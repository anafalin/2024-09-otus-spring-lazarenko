package ru.otus.hw.service;

public interface AuthService {
    void login(String firstName, String lastName);

    boolean isUserLoggedIn();
}