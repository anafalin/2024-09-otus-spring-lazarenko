package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.security.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}