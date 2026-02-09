package ru.itmo.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.auth.entity.User;
import ru.itmo.auth.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/internal")
@RequiredArgsConstructor
public class InternalController {

    private final UserRepository userRepository;

    @PostMapping("/emails")
    public ResponseEntity<Map<String, String>> getEmailsByUserIds(@RequestBody List<String> userIds) {
        List<UUID> uuids ${DB_USER:***REMOVED***} userIds.stream()
                .map(UUID::fromString)
                .collect(Collectors.toList());

        List<User> users ${DB_USER:***REMOVED***} userRepository.findByIdIn(uuids);

        Map<String, String> result ${DB_USER:***REMOVED***} users.stream()
                .collect(Collectors.toMap(
                        u -> u.getId().toString(),
                        User::getEmail
                ));

        return ResponseEntity.ok(result);
    }
}
