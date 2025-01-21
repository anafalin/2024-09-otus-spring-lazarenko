package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.UserAppInfoDto;
import ru.otus.hw.dto.UserRoleUpdateDto;
import ru.otus.hw.repositories.RoleRepository;
import ru.otus.hw.security.Role;
import ru.otus.hw.services.UserAppService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserAppService userAppService;

    private final RoleRepository roleRepository;

    @GetMapping("/users")
    public String getAllUsers(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        Pageable paging = PageRequest.of(page - 1, size);
        Page<UserAppInfoDto> pageUsers = userAppService.findAll(paging);

        List<UserAppInfoDto> users = pageUsers.getContent();

        model.addAttribute("userList", users);
        model.addAttribute("currentPage", pageUsers.getNumber() + 1);
        model.addAttribute("totalItems", pageUsers.getTotalElements());
        model.addAttribute("totalPages", pageUsers.getTotalPages());
        model.addAttribute("pageSize", size);

        return "/admin/user/get-all";
    }

    @GetMapping("/users/{userId}/roles/change")
    public String getPageUserRolesEdit(@PathVariable long userId, Model model) {
        List<Role> currentRoles = userAppService.getUserRolesByUserId(userId);
        UserRoleUpdateDto request = new UserRoleUpdateDto(userId, new ArrayList<>(3));

        model.addAttribute("currentRoles", currentRoles);
        model.addAttribute("request", request);
        model.addAttribute("roles", roleRepository.findAll());

        return "/admin/user/change-role";
    }

    @PostMapping("users/roles/update")
    public String updateUserRoles(@ModelAttribute("request") UserRoleUpdateDto request) {
        userAppService.updateUserRolesByUserId(request.getId(), request.getNewRoles());
        return "redirect:/admin/users/" + request.getId() + "/roles/change";
    }
}