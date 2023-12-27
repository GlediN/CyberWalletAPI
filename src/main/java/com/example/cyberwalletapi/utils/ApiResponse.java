package com.example.cyberwalletapi.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor

public class ApiResponse<T> {
    private T data;
    private String error;

    // Constructors, getters, and setters

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> error(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setError(error);
        return response;
    }
}
