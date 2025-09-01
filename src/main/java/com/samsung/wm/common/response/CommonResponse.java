package com.samsung.wm.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 공통 응답 클래스
 * 모든 모듈 서비스의 통일된 응답 형식
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    
    private boolean success;
    private String code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(
            true,
            "0000",
            "처리 성공",
            data,
            LocalDateTime.now()
        );
    }
    
    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(
            true,
            "0000",
            message,
            data,
            LocalDateTime.now()
        );
    }
    
    public static <T> CommonResponse<T> error(String code, String message) {
        return new CommonResponse<>(
            false,
            code,
            message,
            null,
            LocalDateTime.now()
        );
    }
    
    public static <T> CommonResponse<T> error(String message) {
        return error("9999", message);
    }
}