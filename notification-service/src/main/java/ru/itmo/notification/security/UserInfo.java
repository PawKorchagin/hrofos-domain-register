package ru.itmo.notification.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserInfo {
    private final UUID userId;
    private final String email;
    private final Boolean isAdmin;
}
