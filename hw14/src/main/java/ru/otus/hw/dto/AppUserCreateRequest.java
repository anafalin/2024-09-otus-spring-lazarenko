package ru.otus.hw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUserCreateRequest {
    @NotEmpty(message = "Введите логин")
    @Pattern(regexp = "[A-Za-z0-9]{5,15}",
            message = "Имя пользователя (логин) должен быть длинной от 5 до 15 символов и состоять из букв и цифр")
    private String username;

    @NotEmpty(message = "Введите пароль")
    @Pattern(regexp = "[A-Za-z0-9._]{5,15}",
            message = "Пароль должен содержать от 5 до 15 символов и состоять из заглавных, строчных букв и цифр")
    private String password;

    @NotEmpty(message = "Введите подтверждение пароля")
    private String passwordConfirm;

    @Email(message = "Неверный формат email (example@mail.com)")
    @NotEmpty(message = "Введите email")
    private String email;

    @NotEmpty(message = "Введите имя")
    private String firstname;

    @NotEmpty(message = "Введите фамилию")
    private String lastname;
}