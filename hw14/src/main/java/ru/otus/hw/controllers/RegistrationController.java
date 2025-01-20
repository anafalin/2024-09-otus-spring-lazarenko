package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AppUserCreateRequest;
import ru.otus.hw.exceptions.UserRegDataNotUniqueException;
import ru.otus.hw.services.UserAppService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserAppService userAppService;

    @GetMapping("/signup")
    public String getPageSignUp(Model model) {
        model.addAttribute("user", new AppUserCreateRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String createNewClient(@ModelAttribute("user") @Valid AppUserCreateRequest user,
                                  BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            return "signup";
        }

        try {
            userAppService.checkIsUniqueLoginAndEmail(user.getUsername(), user.getEmail());
        } catch (UserRegDataNotUniqueException exception) {
            model.addAttribute("userRegDataError", exception.getMessage());
            return "signup";
        }

        if (user.getPassword() != null && !user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("passwordConfirmError", "Введенные пароли не совпадают");
            return "signup";
        }

        userAppService.createUser(user);
        return "login";
    }
}