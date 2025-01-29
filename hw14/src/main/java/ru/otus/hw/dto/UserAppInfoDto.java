package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.hw.security.Role;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAppInfoDto {
    private Long id;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private Boolean isEnabled;

    private Boolean isAccountLocked;

    private List<Role> roles;
}