package ru.itmo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message ${DB_USER:***REMOVED***} "Email is required")
    @Email(message ${DB_USER:***REMOVED***} "Email must be valid")
    private String email;

    @NotBlank(message ${DB_USER:***REMOVED***} "Password is required")
    @Size(min ${DB_USER:***REMOVED***} 8, message ${DB_USER:***REMOVED***} "Password must be at least 8 characters")
    private String password;
}
