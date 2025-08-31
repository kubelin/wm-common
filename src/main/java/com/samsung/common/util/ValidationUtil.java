package com.samsung.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 데이터 검증 유틸리티
 * C의 검증 함수들과 유사한 기능 제공
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil {
    
    // 정규식 패턴들
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern POSITIVE_INTEGER_PATTERN = Pattern.compile("^[1-9]\\d*$");
    private static final Pattern KOREAN_PATTERN = Pattern.compile("^[가-힣]+$");
    private static final Pattern ENGLISH_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    
    // 금융 관련 패턴들
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^\\d{10,16}$");
    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{13,19}$");
    private static final Pattern BUSINESS_NUMBER_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{5}$");
    
    /**
     * null 또는 빈 값 체크
     * C의 NULL 체크와 유사
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }
    
    /**
     * null이 아닌지 체크
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    /**
     * 컬렉션이 비어있는지 체크
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    /**
     * 컬렉션이 비어있지 않은지 체크
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * Map이 비어있는지 체크
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
    
    /**
     * Map이 비어있지 않은지 체크
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    /**
     * 배열이 비어있는지 체크
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
    
    /**
     * 배열이 비어있지 않은지 체크
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
    
    /**
     * 숫자 형태 문자열인지 확인
     * C의 isdigit() 확장 버전
     */
    public static boolean isNumeric(String str) {
        return StringUtil.isNotEmpty(str) && NUMERIC_PATTERN.matcher(str).matches();
    }
    
    /**
     * 정수 형태 문자열인지 확인
     */
    public static boolean isInteger(String str) {
        return StringUtil.isNotEmpty(str) && INTEGER_PATTERN.matcher(str).matches();
    }
    
    /**
     * 양의 정수인지 확인
     */
    public static boolean isPositiveInteger(String str) {
        return StringUtil.isNotEmpty(str) && POSITIVE_INTEGER_PATTERN.matcher(str).matches();
    }
    
    /**
     * 한글만 포함하는지 확인
     */
    public static boolean isKorean(String str) {
        return StringUtil.isNotEmpty(str) && KOREAN_PATTERN.matcher(str).matches();
    }
    
    /**
     * 영문자만 포함하는지 확인
     * C의 isalpha()와 유사
     */
    public static boolean isAlpha(String str) {
        return StringUtil.isNotEmpty(str) && ENGLISH_PATTERN.matcher(str).matches();
    }
    
    /**
     * 영문자와 숫자만 포함하는지 확인
     * C의 isalnum()과 유사
     */
    public static boolean isAlphaNumeric(String str) {
        return StringUtil.isNotEmpty(str) && ALPHANUMERIC_PATTERN.matcher(str).matches();
    }
    
    /**
     * 값이 지정된 범위 안에 있는지 확인
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * 값이 지정된 범위 안에 있는지 확인 (double)
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * 문자열 길이가 지정된 범위 안에 있는지 확인
     */
    public static boolean isLengthInRange(String str, int minLength, int maxLength) {
        if (StringUtil.isEmpty(str)) return false;
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * 계좌번호 형식 검증
     * 10-16자리 숫자
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        return StringUtil.isNotEmpty(accountNumber) && 
               ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }
    
    /**
     * 카드번호 형식 검증 (기본적인 형태만)
     * 13-19자리 숫자
     */
    public static boolean isValidCardNumber(String cardNumber) {
        return StringUtil.isNotEmpty(cardNumber) && 
               CARD_NUMBER_PATTERN.matcher(cardNumber.replaceAll("[\\s-]", "")).matches();
    }
    
    /**
     * 사업자번호 형식 검증
     * 000-00-00000 형태
     */
    public static boolean isValidBusinessNumber(String businessNumber) {
        return StringUtil.isNotEmpty(businessNumber) && 
               BUSINESS_NUMBER_PATTERN.matcher(businessNumber).matches();
    }
    
    /**
     * 주민등록번호 앞자리 형식 검증 (생년월일)
     * YYMMDD 형태
     */
    public static boolean isValidBirthDate6Digits(String birthDate) {
        if (StringUtil.isEmpty(birthDate) || birthDate.length() != 6) {
            return false;
        }
        
        if (!isNumeric(birthDate)) {
            return false;
        }
        
        int month = Integer.parseInt(birthDate.substring(2, 4));
        int day = Integer.parseInt(birthDate.substring(4, 6));
        
        return isInRange(month, 1, 12) && isInRange(day, 1, 31);
    }
    
    /**
     * 금액이 유효한지 확인 (양수, 소수점 2자리까지)
     */
    public static boolean isValidAmount(String amount) {
        if (StringUtil.isEmpty(amount)) return false;
        
        try {
            double value = Double.parseDouble(amount);
            if (value < 0) return false;
            
            // 소수점 2자리까지만 허용
            String[] parts = amount.split("\\.");
            if (parts.length > 1 && parts[1].length() > 2) {
                return false;
            }
            
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 금액이 지정된 범위 내에 있는지 확인
     */
    public static boolean isAmountInRange(String amount, double minAmount, double maxAmount) {
        if (!isValidAmount(amount)) return false;
        
        try {
            double value = Double.parseDouble(amount);
            return isInRange(value, minAmount, maxAmount);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 필수값 체크 (multiple values)
     * C의 여러 파라미터 체크와 유사
     */
    public static boolean hasRequiredValues(Object... values) {
        if (isEmpty(values)) return false;
        
        for (Object value : values) {
            if (value == null) return false;
            if (value instanceof String && StringUtil.isEmpty((String) value)) return false;
            if (value instanceof Collection && isEmpty((Collection<?>) value)) return false;
            if (value instanceof Map && isEmpty((Map<?, ?>) value)) return false;
        }
        
        return true;
    }
    
    /**
     * 두 값이 같은지 확인 (null safe)
     */
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) return true;
        if (obj1 == null || obj2 == null) return false;
        return obj1.equals(obj2);
    }
    
    /**
     * 문자열이 최소 길이를 만족하는지 확인
     */
    public static boolean hasMinLength(String str, int minLength) {
        return StringUtil.isNotEmpty(str) && str.length() >= minLength;
    }
    
    /**
     * 문자열이 최대 길이를 초과하지 않는지 확인
     */
    public static boolean hasMaxLength(String str, int maxLength) {
        return str == null || str.length() <= maxLength;
    }
}