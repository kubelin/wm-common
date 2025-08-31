package com.samsung.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);
    
    private DateTimeUtil() {
        // Utility class
    }
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }
    
    public static LocalDate parseDate(String dateString) {
        try {
            return dateString != null ? LocalDate.parse(dateString, DATE_FORMATTER) : null;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: " + DATE_PATTERN, e);
        }
    }
    
    public static LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return dateTimeString != null ? LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER) : null;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format. Expected: " + DATETIME_PATTERN, e);
        }
    }
    
    public static boolean isMarketDay(LocalDate date) {
        // Simple implementation - excludes weekends
        // In real implementation, this would check market holidays
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5; // Monday to Friday
    }
}