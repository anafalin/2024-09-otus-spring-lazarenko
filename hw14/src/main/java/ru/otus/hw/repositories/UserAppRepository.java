package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.models.UserApp;
import ru.otus.hw.security.Role;

import java.util.List;
import java.util.Optional;

public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    Optional<UserApp> findUserAppByUsername(String username);

    boolean existsUserAppByUsername(String username);

    boolean existsUserAppByEmail(String email);

    @Query(value = "SELECT u.roles FROM UserApp u WHERE u.id =:id")
    List<Role> findAllRolesByUserId(long id);

    Optional<UserApp> findUserAppById(Long id);
}