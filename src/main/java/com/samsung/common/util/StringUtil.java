package com.samsung.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 문자열 처리 유틸리티
 * C의 string.h와 유사한 기능들을 제공
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$"
    );
    
    /**
     * 문자열이 비어있거나 null인지 확인
     * C의 strlen() == 0 체크와 유사
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 문자열이 비어있지 않은지 확인
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 안전한 문자열 비교 (null safe)
     * C의 strcmp()와 유사하지만 null 처리
     */
    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }
    
    /**
     * 대소문자 무시 문자열 비교
     * C의 strcasecmp()와 유사
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equalsIgnoreCase(str2);
    }
    
    /**
     * 문자열 좌우 공백 제거 후 기본값 반환
     * C의 공통 패턴: if (str != NULL && strlen(str) > 0) 처리
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str.trim();
    }
    
    /**
     * 문자열 마스킹 (개인정보 보호)
     * 예: "홍길동" → "홍*동", "010-1234-5678" → "010-****-5678"
     */
    public static String mask(String str, char maskChar, int startPos, int endPos) {
        if (isEmpty(str) || startPos >= str.length()) {
            return str;
        }
        
        StringBuilder sb = new StringBuilder(str);
        int actualEndPos = Math.min(endPos, str.length());
        
        for (int i = startPos; i < actualEndPos; i++) {
            sb.setCharAt(i, maskChar);
        }
        
        return sb.toString();
    }
    
    /**
     * 이메일 형식 검증
     * C의 regex 검증과 유사
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 휴대폰 번호 형식 검증
     */
    public static boolean isValidPhoneNumber(String phone) {
        return isNotEmpty(phone) && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 문자열을 지정된 길이로 좌측 패딩
     * C의 sprintf() 패딩과 유사
     */
    public static String padLeft(String str, int totalLength, char padChar) {
        if (str == null) str = "";
        if (str.length() >= totalLength) return str;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalLength - str.length(); i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }
    
    /**
     * 문자열을 지정된 길이로 우측 패딩
     */
    public static String padRight(String str, int totalLength, char padChar) {
        if (str == null) str = "";
        if (str.length() >= totalLength) return str;
        
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < totalLength - str.length(); i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }
    
    /**
     * 문자열 배열을 구분자로 연결
     * C의 문자열 연결 함수들과 유사
     */
    public static String join(List<String> strings, String delimiter) {
        if (strings == null || strings.isEmpty()) return "";
        return String.join(delimiter, strings);
    }
    
    /**
     * 문자열에서 숫자만 추출
     * 예: "010-1234-5678" → "01012345678"
     */
    public static String extractNumbers(String str) {
        if (isEmpty(str)) return "";
        return str.replaceAll("[^0-9]", "");
    }
    
    /**
     * 문자열에서 영문자만 추출
     */
    public static String extractAlpha(String str) {
        if (isEmpty(str)) return "";
        return str.replaceAll("[^a-zA-Z]", "");
    }
    
    /**
     * 바이트 길이 계산 (한글 등 멀티바이트 고려)
     * C의 strlen()과 다르게 실제 바이트 크기 계산
     */
    public static int getByteLength(String str, String charset) {
        if (isEmpty(str)) return 0;
        try {
            return str.getBytes(charset).length;
        } catch (Exception e) {
            return str.length();
        }
    }
    
    /**
     * UTF-8 바이트 길이 계산
     */
    public static int getByteLength(String str) {
        return getByteLength(str, "UTF-8");
    }
}