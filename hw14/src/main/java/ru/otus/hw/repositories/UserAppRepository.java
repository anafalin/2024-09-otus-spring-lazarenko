package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.UserApp;

import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    Optional<UserApp> findUserAppByUsername(String username);

    boolean existsUserAppByUsername(String username);

    boolean existsUserAppByEmail(String email);
}