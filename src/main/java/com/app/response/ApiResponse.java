package com.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

// generic response format that respects the FE guidelines

// { "success": true, "data": ..., "total": ... } for success
// { "success": false, "error": {...}, "timestamp": ..., "path": ... } for failure

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // hides fields that don't apply to a response instead of serializing them as null
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private Integer total;
    private String message; // for delete/action responses with no data payload
    private ApiError error;
    private String timestamp;
    private String path;

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }
    public static <T> ApiResponse<T> ok(T data, int total) {
        ApiResponse<T> response = ok(data);
        response.setTotal(total);
        return response;
    }

    public static ApiResponse<Void> message(String message) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }
}