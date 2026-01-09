package ru.itmo.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;
    private final ApiError error;

    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, null);
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return error(error, null);
    }

    public static <T> ApiResponse<T> error(ApiError error, String message) {
        return new ApiResponse<>(false, null, message, error);
    }
}
