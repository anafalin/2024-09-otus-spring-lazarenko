package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService, StudentContextService {
    private Student student;

    @Override
    public void login(String firstName, String lastName) {
        student = new Student(firstName, lastName);
    }

    @Override
    public boolean isUserLoggedIn() {
        return Objects.nonNull(student);
    }

    @Override
    public Student getUser() {
        return student;
    }
}