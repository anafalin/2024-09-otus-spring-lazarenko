package ru.otus.hw.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AppUserCreateRequest;
import ru.otus.hw.dto.UserAppInfoDto;
import ru.otus.hw.exceptions.UserNotFoundException;
import ru.otus.hw.exceptions.UserRegDataNotUniqueException;
import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.models.UserApp;
import ru.otus.hw.repositories.RoleRepository;
import ru.otus.hw.repositories.UserAppRepository;
import ru.otus.hw.security.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserAppRepository userAppRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

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

    @Transactional(readOnly = true)
    public Page<UserAppInfoDto> findAll(Pageable paging) {
        Page<UserApp> booksPage = userAppRepository.findAll(paging);
        return new PageImpl<>(userMapper.toUserAppInfoDtos(booksPage.getContent()));
    }

    @Transactional(readOnly = true)
    public List<Role> getUserRolesByUserId(long userId) {
        return userAppRepository.findAllRolesByUserId(userId);
    }

    @Transactional
    public void updateUserRolesByUserId(Long userId, List<Integer> newRoles) {
        UserApp user = userAppRepository.findUserAppById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id='%s' not found".formatted(userId)));

        List<Role> roles = roleRepository.findAllById(newRoles);

        user.setRoles(roles);
        userAppRepository.save(user);
    }
}