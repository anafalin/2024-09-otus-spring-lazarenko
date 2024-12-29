package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.security.UserApp;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    Optional<UserApp> findUserAppByUsername(String username);
}