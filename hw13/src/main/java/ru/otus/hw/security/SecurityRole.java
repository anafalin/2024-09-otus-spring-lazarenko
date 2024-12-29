package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;

    @Override
    public String getAuthority() {
        String prefix = "ROLE_";
        String name = role.getName();

        if (!name.startsWith(prefix)) {
            name = prefix.concat(name);
        }
        return name;
    }
}