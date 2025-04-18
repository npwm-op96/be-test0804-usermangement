package com.th.test0804.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {

    private String messageCode;
    private T data;
    private String message;
    private Long totalElements;
    // Constructors
    public ApiResponse(String messageCode, T data, String message) {

        this.messageCode = messageCode;
        this.data = data;
        this.message = message;
    }
    // Static helper methods to easily generate common response types
    public static <T> ApiResponse<T> success(String status, T data, String message) {
        return new ApiResponse<>(status, data, message);
    }

    public static <T> ApiResponse<T> error(String status, T data, String message) {
        return new ApiResponse<>(status, data, message);
    }
}
