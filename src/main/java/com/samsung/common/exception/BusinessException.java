package com.samsung.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final String message;
    
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}