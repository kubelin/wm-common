package com.samsung.wm.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 날짜/시간 유틸리티 클래스
 */
public final class DateTimeUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private DateTimeUtil() {
        // 인스턴스 생성 방지
    }
    
    /**
     * 현재 날짜를 문자열로 반환
     * 
     * @return 현재 날짜 문자열 (yyyy-MM-dd)
     */
    public static String getCurrentDateString() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
    /**
     * 현재 날짜시간을 문자열로 반환
     * 
     * @return 현재 날짜시간 문자열 (yyyy-MM-dd HH:mm:ss)
     */
    public static String getCurrentDateTimeString() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
    
    /**
     * 날짜 문자열을 LocalDate로 변환
     * 
     * @param dateString 날짜 문자열 (yyyy-MM-dd)
     * @return LocalDate 객체
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    /**
     * 날짜시간 문자열을 LocalDateTime으로 변환
     * 
     * @param dateTimeString 날짜시간 문자열 (yyyy-MM-dd HH:mm:ss)
     * @return LocalDateTime 객체
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
    }
    
    /**
     * 두 날짜 간의 일수 차이 계산
     * 
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 일수 차이
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
    
    /**
     * 나이 계산
     * 
     * @param birthDate 생년월일
     * @return 나이
     */
    public static int calculateAge(LocalDate birthDate) {
        return (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }
    
    /**
     * 영업일 여부 확인 (주말 제외)
     * 
     * @param date 확인할 날짜
     * @return 영업일 여부
     */
    public static boolean isBusinessDay(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5; // Monday(1) to Friday(5)
    }
    
    /**
     * 다음 영업일 계산
     * 
     * @param date 기준 날짜
     * @return 다음 영업일
     */
    public static LocalDate getNextBusinessDay(LocalDate date) {
        LocalDate nextDay = date.plusDays(1);
        while (!isBusinessDay(nextDay)) {
            nextDay = nextDay.plusDays(1);
        }
        return nextDay;
    }
    
    /**
     * 마지막 리밸런싱 이후 경과 일수 계산
     * 
     * @param lastRebalancingDate 마지막 리밸런싱 날짜
     * @return 경과 일수
     */
    public static long daysSinceLastRebalancing(LocalDateTime lastRebalancingDate) {
        return ChronoUnit.DAYS.between(lastRebalancingDate.toLocalDate(), LocalDate.now());
    }
}