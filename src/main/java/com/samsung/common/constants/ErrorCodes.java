package com.samsung.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 에러 코드 상수 정의
 * C의 ERROR_CODE와 유사한 개념
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodes {
    
    // 공통 에러 코드 (0000-0999)
    public static final String SUCCESS = "0000";
    public static final String UNKNOWN_ERROR = "0001";
    public static final String INVALID_PARAMETER = "0002";
    public static final String NULL_PARAMETER = "0003";
    public static final String INVALID_FORMAT = "0004";
    public static final String CONVERSION_ERROR = "0005";
    public static final String VALIDATION_ERROR = "0006";
    public static final String CALCULATION_ERROR = "0007";
    public static final String DATA_NOT_FOUND = "0008";
    public static final String DUPLICATE_DATA = "0009";
    
    // 인증/인가 에러 (1000-1999)
    public static final String UNAUTHORIZED = "1001";
    public static final String FORBIDDEN = "1002";
    public static final String INVALID_TOKEN = "1003";
    public static final String EXPIRED_TOKEN = "1004";
    public static final String LOGIN_FAILED = "1005";
    public static final String ACCOUNT_LOCKED = "1006";
    public static final String PASSWORD_EXPIRED = "1007";
    public static final String INSUFFICIENT_PRIVILEGES = "1008";
    
    // 비즈니스 로직 에러 (2000-2999)
    public static final String BUSINESS_RULE_VIOLATION = "2001";
    public static final String INSUFFICIENT_BALANCE = "2002";
    public static final String TRANSACTION_LIMIT_EXCEEDED = "2003";
    public static final String INVALID_TRANSACTION = "2004";
    public static final String ACCOUNT_SUSPENDED = "2005";
    public static final String INVALID_ACCOUNT_STATUS = "2006";
    public static final String TRANSACTION_NOT_ALLOWED = "2007";
    public static final String RISK_LIMIT_EXCEEDED = "2008";
    
    // 상담 관련 에러 (3000-3999)
    public static final String CONSULTATION_NOT_FOUND = "3001";
    public static final String INVALID_CONSULTATION_TYPE = "3002";
    public static final String CONSULTATION_ALREADY_EXISTS = "3003";
    public static final String CONSULTATION_NOT_AVAILABLE = "3004";
    public static final String INVALID_CONSULTATION_STATUS = "3005";
    public static final String CONSULTATION_EXPIRED = "3006";
    public static final String CONSULTATION_CANCELLED = "3007";
    
    // 투자 관련 에러 (4000-4999)
    public static final String INVESTMENT_NOT_FOUND = "4001";
    public static final String INVALID_INVESTMENT_TYPE = "4002";
    public static final String INVESTMENT_LIMIT_EXCEEDED = "4003";
    public static final String INSUFFICIENT_INVESTMENT_AMOUNT = "4004";
    public static final String INVALID_RISK_PROFILE = "4005";
    public static final String INVESTMENT_NOT_ALLOWED = "4006";
    public static final String MARKET_CLOSED = "4007";
    public static final String PRICE_LIMIT_EXCEEDED = "4008";
    
    // 포트폴리오 관련 에러 (5000-5999)
    public static final String PORTFOLIO_NOT_FOUND = "5001";
    public static final String INVALID_PORTFOLIO_TYPE = "5002";
    public static final String PORTFOLIO_REBALANCING_FAILED = "5003";
    public static final String INVALID_ASSET_ALLOCATION = "5004";
    public static final String PORTFOLIO_LOCKED = "5005";
    public static final String REBALANCING_NOT_REQUIRED = "5006";
    public static final String INVALID_PORTFOLIO_STATUS = "5007";
    
    // 고객 관련 에러 (6000-6999)
    public static final String CUSTOMER_NOT_FOUND = "6001";
    public static final String CUSTOMER_ALREADY_EXISTS = "6002";
    public static final String INVALID_CUSTOMER_STATUS = "6003";
    public static final String CUSTOMER_VERIFICATION_FAILED = "6004";
    public static final String INVALID_CUSTOMER_TYPE = "6005";
    public static final String CUSTOMER_SUSPENDED = "6006";
    public static final String KYC_NOT_COMPLETED = "6007";
    
    // 시스템 에러 (7000-7999)
    public static final String DATABASE_ERROR = "7001";
    public static final String NETWORK_ERROR = "7002";
    public static final String TIMEOUT_ERROR = "7003";
    public static final String SERVICE_UNAVAILABLE = "7004";
    public static final String EXTERNAL_API_ERROR = "7005";
    public static final String CONFIGURATION_ERROR = "7006";
    public static final String RESOURCE_LIMIT_EXCEEDED = "7007";
    public static final String MAINTENANCE_MODE = "7008";
    
    // 데이터 검증 에러 (8000-8999)
    public static final String INVALID_EMAIL_FORMAT = "8001";
    public static final String INVALID_PHONE_FORMAT = "8002";
    public static final String INVALID_DATE_FORMAT = "8003";
    public static final String INVALID_AMOUNT_FORMAT = "8004";
    public static final String INVALID_ACCOUNT_NUMBER = "8005";
    public static final String INVALID_CARD_NUMBER = "8006";
    public static final String INVALID_BUSINESS_NUMBER = "8007";
    public static final String DATA_LENGTH_EXCEEDED = "8008";
    public static final String REQUIRED_FIELD_MISSING = "8009";
    
    // 파일 처리 에러 (9000-9999)
    public static final String FILE_NOT_FOUND = "9001";
    public static final String FILE_READ_ERROR = "9002";
    public static final String FILE_WRITE_ERROR = "9003";
    public static final String INVALID_FILE_FORMAT = "9004";
    public static final String FILE_SIZE_EXCEEDED = "9005";
    public static final String FILE_UPLOAD_FAILED = "9006";
    public static final String FILE_PROCESSING_ERROR = "9007";
    
    /**
     * 에러 코드가 성공인지 확인
     */
    public static boolean isSuccess(String errorCode) {
        return SUCCESS.equals(errorCode);
    }
    
    /**
     * 에러 코드가 시스템 에러인지 확인
     */
    public static boolean isSystemError(String errorCode) {
        if (errorCode == null || errorCode.length() < 4) {
            return false;
        }
        String prefix = errorCode.substring(0, 1);
        return "7".equals(prefix);
    }
    
    /**
     * 에러 코드가 비즈니스 에러인지 확인
     */
    public static boolean isBusinessError(String errorCode) {
        if (errorCode == null || errorCode.length() < 4) {
            return false;
        }
        String prefix = errorCode.substring(0, 1);
        return "2".equals(prefix) || "3".equals(prefix) || "4".equals(prefix) || "5".equals(prefix) || "6".equals(prefix);
    }
    
    /**
     * 에러 코드가 인증 관련 에러인지 확인
     */
    public static boolean isAuthError(String errorCode) {
        if (errorCode == null || errorCode.length() < 4) {
            return false;
        }
        String prefix = errorCode.substring(0, 1);
        return "1".equals(prefix);
    }
    
    /**
     * 에러 코드가 검증 에러인지 확인
     */
    public static boolean isValidationError(String errorCode) {
        if (errorCode == null || errorCode.length() < 4) {
            return false;
        }
        String prefix = errorCode.substring(0, 1);
        return "8".equals(prefix);
    }
}