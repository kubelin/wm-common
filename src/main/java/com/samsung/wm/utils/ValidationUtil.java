package com.samsung.wm.utils;

import com.samsung.wm.constants.WealthManagementConstants;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 검증 유틸리티 클래스
 */
public final class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$");
    
    private ValidationUtil() {
        // 인스턴스 생성 방지
    }
    
    /**
     * 이메일 형식 검증
     * 
     * @param email 이메일
     * @return 유효 여부
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 전화번호 형식 검증
     * 
     * @param phoneNumber 전화번호
     * @return 유효 여부
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber.replaceAll("-", "")).matches();
    }
    
    /**
     * 고객 ID 검증
     * 
     * @param customerId 고객 ID
     * @return 유효 여부
     */
    public static boolean isValidCustomerId(String customerId) {
        return customerId != null && !customerId.trim().isEmpty() && customerId.length() >= 3;
    }
    
    /**
     * 투자 금액 검증
     * 
     * @param amount 투자 금액
     * @return 유효 여부
     */
    public static boolean isValidInvestmentAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        
        return amount.compareTo(new BigDecimal(WealthManagementConstants.MIN_INVESTMENT_AMOUNT)) >= 0 &&
               amount.compareTo(new BigDecimal(WealthManagementConstants.MAX_INVESTMENT_AMOUNT)) <= 0;
    }
    
    /**
     * 위험 성향 검증
     * 
     * @param riskProfile 위험 성향
     * @return 유효 여부
     */
    public static boolean isValidRiskProfile(String riskProfile) {
        if (riskProfile == null || riskProfile.trim().isEmpty()) {
            return false;
        }
        
        return Arrays.asList(
            WealthManagementConstants.RISK_PROFILE_CONSERVATIVE,
            WealthManagementConstants.RISK_PROFILE_MODERATE,
            WealthManagementConstants.RISK_PROFILE_AGGRESSIVE
        ).contains(riskProfile.toLowerCase());
    }
    
    /**
     * 투자 기간 검증
     * 
     * @param investmentPeriod 투자 기간
     * @return 유효 여부
     */
    public static boolean isValidInvestmentPeriod(String investmentPeriod) {
        if (investmentPeriod == null || investmentPeriod.trim().isEmpty()) {
            return false;
        }
        
        return Arrays.asList(
            WealthManagementConstants.INVESTMENT_PERIOD_SHORT,
            WealthManagementConstants.INVESTMENT_PERIOD_MEDIUM,
            WealthManagementConstants.INVESTMENT_PERIOD_LONG
        ).contains(investmentPeriod.toLowerCase());
    }
    
    /**
     * 자산 배분 비중 검증
     * 
     * @param weight 비중
     * @return 유효 여부
     */
    public static boolean isValidAllocationWeight(Double weight) {
        if (weight == null) {
            return false;
        }
        
        return weight >= WealthManagementConstants.MIN_ALLOCATION_WEIGHT &&
               weight <= WealthManagementConstants.MAX_ALLOCATION_WEIGHT;
    }
    
    /**
     * 포트폴리오 ID 검증
     * 
     * @param portfolioId 포트폴리오 ID
     * @return 유효 여부
     */
    public static boolean isValidPortfolioId(String portfolioId) {
        return portfolioId != null && !portfolioId.trim().isEmpty() && portfolioId.length() >= 3;
    }
}