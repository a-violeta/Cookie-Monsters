package com.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // This ensures null fields are omitted from the JSON
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;

    // Use this when returning objects (like User Profile or Auth Token)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // Use this when returning simple success messages (like password changes)
    public static <T> ApiResponse<T> successMessage(String message) {
        return new ApiResponse<>(true, null, message);
    }

    // Use this for global exception handling later
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}