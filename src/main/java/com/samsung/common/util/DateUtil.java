package com.samsung.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * 날짜/시간 처리 유틸리티
 * C의 time.h와 유사한 기능들을 제공
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {
    
    // 자주 사용되는 날짜 포맷들
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";
    public static final String COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
    private static final DateTimeFormatter COMPACT_DATE_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATE_FORMAT);
    private static final DateTimeFormatter COMPACT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(COMPACT_DATETIME_FORMAT);
    
    /**
     * 현재 시간 반환
     * C의 time(NULL)과 유사
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
    
    /**
     * 현재 날짜 반환
     */
    public static LocalDate today() {
        return LocalDate.now();
    }
    
    /**
     * Unix timestamp 반환
     * C의 time()과 유사
     */
    public static long getTimestamp() {
        return Instant.now().getEpochSecond();
    }
    
    /**
     * Unix timestamp를 LocalDateTime으로 변환
     * C의 localtime()과 유사
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }
    
    /**
     * 날짜 포맷팅 (기본 포맷: yyyy-MM-dd)
     * C의 strftime()과 유사
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
    
    /**
     * 날짜/시간 포맷팅 (기본 포맷: yyyy-MM-dd HH:mm:ss)
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }
    
    /**
     * 커스텀 포맷으로 날짜 포맷팅
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null || StringUtil.isEmpty(pattern)) {
            return null;
        }
        try {
            return dateTime.format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 문자열을 날짜로 파싱 (기본 포맷: yyyy-MM-dd)
     * C의 strptime()과 유사
     */
    public static LocalDate parseDate(String dateStr) {
        if (StringUtil.isEmpty(dateStr)) return null;
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * 문자열을 날짜/시간으로 파싱 (기본 포맷: yyyy-MM-dd HH:mm:ss)
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (StringUtil.isEmpty(dateTimeStr)) return null;
        try {
            return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * 커스텀 포맷으로 날짜/시간 파싱
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        if (StringUtil.isEmpty(dateTimeStr) || StringUtil.isEmpty(pattern)) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * 두 날짜 간의 차이 계산 (일 단위)
     * C의 difftime()과 유사
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * 두 날짜/시간 간의 차이 계산 (초 단위)
     */
    public static long secondsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.SECONDS.between(start, end);
    }
    
    /**
     * 날짜에 일수 추가
     * C의 날짜 계산 함수들과 유사
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date != null ? date.plusDays(days) : null;
    }
    
    /**
     * 날짜에 월수 추가
     */
    public static LocalDate addMonths(LocalDate date, long months) {
        return date != null ? date.plusMonths(months) : null;
    }
    
    /**
     * 날짜에 년수 추가
     */
    public static LocalDate addYears(LocalDate date, long years) {
        return date != null ? date.plusYears(years) : null;
    }
    
    /**
     * 날짜/시간에 시간 추가
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.plusHours(hours) : null;
    }
    
    /**
     * 날짜/시간에 분 추가
     */
    public static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        return dateTime != null ? dateTime.plusMinutes(minutes) : null;
    }
    
    /**
     * 해당 월의 첫날 반환
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date != null ? date.withDayOfMonth(1) : null;
    }
    
    /**
     * 해당 월의 마지막날 반환
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date != null ? date.withDayOfMonth(date.lengthOfMonth()) : null;
    }
    
    /**
     * 영업일인지 확인 (주말 제외)
     */
    public static boolean isWeekday(LocalDate date) {
        if (date == null) return false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    
    /**
     * 주말인지 확인
     */
    public static boolean isWeekend(LocalDate date) {
        return !isWeekday(date);
    }
    
    /**
     * 나이 계산 (만 나이)
     */
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    /**
     * 컴팩트 형태 날짜 문자열 (yyyyMMdd)
     * 예: "20240315"
     */
    public static String toCompactDateString(LocalDate date) {
        return date != null ? date.format(COMPACT_DATE_FORMATTER) : null;
    }
    
    /**
     * 컴팩트 형태 날짜/시간 문자열 (yyyyMMddHHmmss)
     * 예: "20240315143052"
     */
    public static String toCompactDateTimeString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(COMPACT_DATETIME_FORMATTER) : null;
    }
    
    /**
     * 컴팩트 형태 문자열을 날짜로 변환
     */
    public static LocalDate fromCompactDateString(String compactDate) {
        if (StringUtil.isEmpty(compactDate) || compactDate.length() != 8) {
            return null;
        }
        try {
            return LocalDate.parse(compactDate, COMPACT_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}