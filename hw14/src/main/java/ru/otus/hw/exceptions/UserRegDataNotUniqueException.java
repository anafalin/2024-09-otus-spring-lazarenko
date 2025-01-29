package ru.otus.hw.exceptions;

public class UserRegDataNotUniqueException extends RuntimeException {
    public UserRegDataNotUniqueException(String message) {
        super(message);
    }
}