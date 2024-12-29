package ru.otus.hw.controllers;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.security.SecurityUser;

@Controller
public class MainController {

    @GetMapping("/")
    public String getStartPage() {
        return "index";
    }

    @GetMapping("/login")
    public String getPageLogin(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("messageError", "Неверные учетные данные пользователя");
        }
        return "login";
    }

    @GetMapping("/home")
    public String getPageHome(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        SecurityUser user = (SecurityUser) securityContext.getAuthentication().getPrincipal();
        model.addAttribute("username", user.getUsername());
        return "index";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/";
    }
}