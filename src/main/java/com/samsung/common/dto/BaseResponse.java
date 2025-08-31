package com.samsung.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "Success", data, null, LocalDateTime.now());
    }
    
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(true, message, data, null, LocalDateTime.now());
    }
    
    public static <T> BaseResponse<T> error(String message, String errorCode) {
        return new BaseResponse<>(false, message, null, errorCode, LocalDateTime.now());
    }
    
    public static <T> BaseResponse<T> error(String message) {
        return error(message, "GENERAL_ERROR");
    }
}