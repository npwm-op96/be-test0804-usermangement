package com.th.test0804.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiPageResponse<T> {

    private String messageCode;
    private T data;
    private String message;
    private Long totalElements;
    // Constructors
    public ApiPageResponse(String messageCode, T data, String message, long totalElements) {

        this.messageCode = messageCode;
        this.data = data;
        this.message = message;
        this.totalElements = totalElements;
    }
    // Static helper methods to easily generate common response types
    public static <T> ApiPageResponse<T> success(String status, T data, String message, long totalElements) {
        return new ApiPageResponse<>(status, data, message, totalElements);
    }


    public static <T> ApiPageResponse<T> error(String status, T data, String message , long totalElements) {
        return new ApiPageResponse<>(status, data, message,totalElements);
    }
}
