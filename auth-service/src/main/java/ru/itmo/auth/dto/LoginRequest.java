package ru.itmo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message ${DB_USER:***REMOVED***} "Email is required")
    @Email(message ${DB_USER:***REMOVED***} "Email must be valid")
    private String email;

    @NotBlank(message ${DB_USER:***REMOVED***} "Password is required")
    private String password;
}
