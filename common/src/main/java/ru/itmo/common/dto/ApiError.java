package ru.itmo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiError {
    private final String code;
    private final String message;
    private final Map<String, Object> details;

    public ApiError(String code, String message) {
        this(code, message, null);
    }
}
