package ru.otus.hw.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AppUserCreateRequest;
import ru.otus.hw.exceptions.UserRegDataNotUniqueException;
import ru.otus.hw.models.UserApp;
import ru.otus.hw.repositories.UserAppRepository;
import ru.otus.hw.security.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserAppRepository userAppRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public void checkIsUniqueLoginAndEmail(String username, String email) {
        if (userAppRepository.existsUserAppByUsername(username)) {
            throw new UserRegDataNotUniqueException("Пользователь с таким логином уже существует");
        }
        if (userAppRepository.existsUserAppByEmail(email)) {
            throw new UserRegDataNotUniqueException("Пользователь с таким email уже существует");
        }
    }

    @Transactional
    public void createUser(@Valid AppUserCreateRequest newAppUser) {
        Role role = new Role();
        role.setName("USER");

        UserApp appUser = UserApp.builder()
                .username(newAppUser.getUsername())
                .firstname(newAppUser.getFirstname())
                .lastname(newAppUser.getLastname())
                .email(newAppUser.getEmail())
                .password(passwordEncoder.encode(newAppUser.getPassword()))
                .roles(List.of(role))
                .isAccountLocked(false)
                .isEnabled(true)
                .build();

        userAppRepository.save(appUser);
    }
}