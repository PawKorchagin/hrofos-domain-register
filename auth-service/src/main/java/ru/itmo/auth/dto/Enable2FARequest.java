package ru.itmo.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enable2FARequest {
    @NotBlank(message ${DB_USER:***REMOVED***} "TOTP code is required")
    @Pattern(regexp ${DB_USER:***REMOVED***} "^\\d{6}$", message ${DB_USER:***REMOVED***} "TOTP code must be 6 digits")
    private String code;
}
