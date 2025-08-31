package com.samsung.common.converter;

import com.samsung.common.util.StringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 데이터 변환 유틸리티
 * C의 형변환 및 데이터 변환 함수들을 Java로 변환
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataConverter {
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#,##0.00%");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    
    /**
     * 문자열을 정수로 안전하게 변환
     * C의 atoi()와 유사하지만 더 안전함
     */
    public static Integer toInteger(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 문자열을 정수로 변환 (기본값 포함)
     */
    public static int toInteger(String str, int defaultValue) {
        Integer result = toInteger(str);
        return result != null ? result : defaultValue;
    }
    
    /**
     * 문자열을 long으로 안전하게 변환
     * C의 atol()과 유사
     */
    public static Long toLong(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        
        try {
            return Long.parseLong(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 문자열을 long으로 변환 (기본값 포함)
     */
    public static long toLong(String str, long defaultValue) {
        Long result = toLong(str);
        return result != null ? result : defaultValue;
    }
    
    /**
     * 문자열을 double로 안전하게 변환
     * C의 atof()와 유사
     */
    public static Double toDouble(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        
        try {
            // 쉼표 제거 후 변환
            String cleaned = str.trim().replaceAll(",", "");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 문자열을 double로 변환 (기본값 포함)
     */
    public static double toDouble(String str, double defaultValue) {
        Double result = toDouble(str);
        return result != null ? result : defaultValue;
    }
    
    /**
     * 문자열을 BigDecimal로 안전하게 변환
     */
    public static BigDecimal toBigDecimal(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        
        try {
            // 쉼표 제거 후 변환
            String cleaned = str.trim().replaceAll(",", "");
            return new BigDecimal(cleaned);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 문자열을 BigDecimal로 변환 (기본값 포함)
     */
    public static BigDecimal toBigDecimal(String str, BigDecimal defaultValue) {
        BigDecimal result = toBigDecimal(str);
        return result != null ? result : defaultValue;
    }
    
    /**
     * 문자열을 boolean으로 변환
     * C의 boolean 변환과 유사
     */
    public static Boolean toBoolean(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        
        String trimmed = str.trim().toLowerCase();
        
        // true 값들
        if ("true".equals(trimmed) || "1".equals(trimmed) || 
            "yes".equals(trimmed) || "y".equals(trimmed) ||
            "on".equals(trimmed) || "enabled".equals(trimmed)) {
            return true;
        }
        
        // false 값들
        if ("false".equals(trimmed) || "0".equals(trimmed) || 
            "no".equals(trimmed) || "n".equals(trimmed) ||
            "off".equals(trimmed) || "disabled".equals(trimmed)) {
            return false;
        }
        
        return null;
    }
    
    /**
     * 문자열을 boolean으로 변환 (기본값 포함)
     */
    public static boolean toBoolean(String str, boolean defaultValue) {
        Boolean result = toBoolean(str);
        return result != null ? result : defaultValue;
    }
    
    /**
     * 숫자를 통화 형식 문자열로 변환
     * 예: 1234567 → "1,234,567"
     */
    public static String toCurrencyString(Number number) {
        if (number == null) {
            return "0";
        }
        return CURRENCY_FORMAT.format(number);
    }
    
    /**
     * 숫자를 백분율 형식 문자열로 변환
     * 예: 0.1234 → "12.34%"
     */
    public static String toPercentString(Number number) {
        if (number == null) {
            return "0.00%";
        }
        return PERCENT_FORMAT.format(number);
    }
    
    /**
     * 숫자를 소수점 형식 문자열로 변환
     * 예: 1234.567 → "1,234.57"
     */
    public static String toDecimalString(Number number) {
        if (number == null) {
            return "0.00";
        }
        return DECIMAL_FORMAT.format(number);
    }
    
    /**
     * BigDecimal을 반올림하여 정수로 변환
     */
    public static Integer roundToInt(BigDecimal decimal) {
        if (decimal == null) {
            return null;
        }
        return decimal.setScale(0, RoundingMode.HALF_UP).intValue();
    }
    
    /**
     * byte 배열을 16진수 문자열로 변환
     * C의 바이너리 데이터 처리와 유사
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    /**
     * 16진수 문자열을 byte 배열로 변환
     */
    public static byte[] hexToBytes(String hex) {
        if (StringUtil.isEmpty(hex) || hex.length() % 2 != 0) {
            return new byte[0];
        }
        
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        
        for (int i = 0; i < length; i += 2) {
            try {
                bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) +
                                      Character.digit(hex.charAt(i + 1), 16));
            } catch (Exception e) {
                return new byte[0];
            }
        }
        
        return bytes;
    }
    
    /**
     * 문자열을 Map으로 변환 (key=value 형식)
     * 예: "name=홍길동,age=30" → Map{"name":"홍길동", "age":"30"}
     */
    public static Map<String, String> stringToMap(String str, String entryDelimiter, String keyValueDelimiter) {
        Map<String, String> map = new HashMap<>();
        
        if (StringUtil.isEmpty(str)) {
            return map;
        }
        
        String[] entries = str.split(entryDelimiter);
        for (String entry : entries) {
            if (StringUtil.isNotEmpty(entry) && entry.contains(keyValueDelimiter)) {
                String[] keyValue = entry.split(keyValueDelimiter, 2);
                if (keyValue.length == 2) {
                    map.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
        
        return map;
    }
    
    /**
     * Map을 문자열로 변환
     */
    public static String mapToString(Map<String, String> map, String entryDelimiter, String keyValueDelimiter) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        
        return map.entrySet().stream()
            .map(entry -> entry.getKey() + keyValueDelimiter + entry.getValue())
            .reduce((s1, s2) -> s1 + entryDelimiter + s2)
            .orElse("");
    }
    
    /**
     * 안전한 나눗셈 (0으로 나누기 방지)
     * C의 안전한 나눗셈과 유사
     */
    public static BigDecimal safeDivide(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (numerator == null || denominator == null || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, scale, RoundingMode.HALF_UP);
    }
    
    /**
     * 안전한 나눗셈 (기본 스케일 4)
     */
    public static BigDecimal safeDivide(BigDecimal numerator, BigDecimal denominator) {
        return safeDivide(numerator, denominator, 4);
    }
    
    /**
     * 객체를 문자열로 안전하게 변환
     */
    public static String toString(Object obj) {
        return obj != null ? obj.toString() : "";
    }
    
    /**
     * 객체를 문자열로 변환 (기본값 포함)
     */
    public static String toString(Object obj, String defaultValue) {
        return obj != null ? obj.toString() : defaultValue;
    }
    
    /**
     * null을 빈 문자열로 변환
     */
    public static String nullToEmpty(String str) {
        return str != null ? str : "";
    }
    
    /**
     * 빈 문자열을 null로 변환
     */
    public static String emptyToNull(String str) {
        return StringUtil.isEmpty(str) ? null : str;
    }
}