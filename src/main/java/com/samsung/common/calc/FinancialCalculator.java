package com.samsung.common.calc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 금융 계산 유틸리티
 * C의 금융 계산 함수들을 Java로 변환
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FinancialCalculator {
    
    private static final int DEFAULT_SCALE = 4;  // 기본 소수점 자리수
    
    /**
     * 단리 계산
     * 공식: A = P(1 + rt)
     * @param principal 원금
     * @param rate 연 이자율 (예: 0.05 = 5%)
     * @param time 기간 (년)
     * @return 만기 금액
     */
    public static BigDecimal simpleInterest(BigDecimal principal, BigDecimal rate, BigDecimal time) {
        if (principal == null || rate == null || time == null) {
            return BigDecimal.ZERO;
        }
        
        // A = P(1 + rt)
        BigDecimal interest = rate.multiply(time);
        BigDecimal factor = BigDecimal.ONE.add(interest);
        
        return principal.multiply(factor).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 복리 계산
     * 공식: A = P(1 + r/n)^(nt)
     * @param principal 원금
     * @param rate 연 이자율
     * @param compoundFrequency 연간 복리 횟수 (예: 12 = 월복리)
     * @param time 기간 (년)
     * @return 만기 금액
     */
    public static BigDecimal compoundInterest(BigDecimal principal, BigDecimal rate, 
                                            int compoundFrequency, BigDecimal time) {
        if (principal == null || rate == null || time == null || compoundFrequency <= 0) {
            return BigDecimal.ZERO;
        }
        
        // A = P(1 + r/n)^(nt)
        BigDecimal ratePerPeriod = rate.divide(BigDecimal.valueOf(compoundFrequency), DEFAULT_SCALE, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(ratePerPeriod);
        
        // n * t 계산
        int totalPeriods = compoundFrequency * time.intValue();
        
        // (1 + r/n)^(nt) 계산
        BigDecimal result = principal;
        for (int i = 0; i < totalPeriods; i++) {
            result = result.multiply(factor);
        }
        
        return result.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 연금 현재가치 계산 (PV of Annuity)
     * 공식: PV = PMT * [(1 - (1 + r)^(-n)) / r]
     * @param payment 정기 지급액
     * @param rate 할인율 (기간별)
     * @param periods 기간 수
     * @return 현재가치
     */
    public static BigDecimal presentValueOfAnnuity(BigDecimal payment, BigDecimal rate, int periods) {
        if (payment == null || rate == null || periods <= 0) {
            return BigDecimal.ZERO;
        }
        
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return payment.multiply(BigDecimal.valueOf(periods));
        }
        
        // (1 + r)^(-n) 계산
        BigDecimal factor = BigDecimal.ONE.add(rate);
        BigDecimal discountFactor = BigDecimal.ONE;
        for (int i = 0; i < periods; i++) {
            discountFactor = discountFactor.divide(factor, DEFAULT_SCALE, RoundingMode.HALF_UP);
        }
        
        // PV = PMT * [(1 - (1 + r)^(-n)) / r]
        BigDecimal numerator = BigDecimal.ONE.subtract(discountFactor);
        BigDecimal pvFactor = numerator.divide(rate, DEFAULT_SCALE, RoundingMode.HALF_UP);
        
        return payment.multiply(pvFactor).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 연금 미래가치 계산 (FV of Annuity)
     * 공식: FV = PMT * [((1 + r)^n - 1) / r]
     * @param payment 정기 지급액
     * @param rate 이자율 (기간별)
     * @param periods 기간 수
     * @return 미래가치
     */
    public static BigDecimal futureValueOfAnnuity(BigDecimal payment, BigDecimal rate, int periods) {
        if (payment == null || rate == null || periods <= 0) {
            return BigDecimal.ZERO;
        }
        
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return payment.multiply(BigDecimal.valueOf(periods));
        }
        
        // (1 + r)^n 계산
        BigDecimal factor = BigDecimal.ONE.add(rate);
        BigDecimal compoundFactor = BigDecimal.ONE;
        for (int i = 0; i < periods; i++) {
            compoundFactor = compoundFactor.multiply(factor);
        }
        
        // FV = PMT * [((1 + r)^n - 1) / r]
        BigDecimal numerator = compoundFactor.subtract(BigDecimal.ONE);
        BigDecimal fvFactor = numerator.divide(rate, DEFAULT_SCALE, RoundingMode.HALF_UP);
        
        return payment.multiply(fvFactor).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 대출 월 상환액 계산 (PMT)
     * 공식: PMT = PV * [r(1 + r)^n] / [(1 + r)^n - 1]
     * @param principal 대출 원금
     * @param monthlyRate 월 이자율
     * @param months 상환 기간 (월)
     * @return 월 상환액
     */
    public static BigDecimal loanPayment(BigDecimal principal, BigDecimal monthlyRate, int months) {
        if (principal == null || monthlyRate == null || months <= 0) {
            return BigDecimal.ZERO;
        }
        
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(months), DEFAULT_SCALE, RoundingMode.HALF_UP);
        }
        
        // (1 + r)^n 계산
        BigDecimal factor = BigDecimal.ONE.add(monthlyRate);
        BigDecimal compoundFactor = BigDecimal.ONE;
        for (int i = 0; i < months; i++) {
            compoundFactor = compoundFactor.multiply(factor);
        }
        
        // PMT = PV * [r(1 + r)^n] / [(1 + r)^n - 1]
        BigDecimal numerator = monthlyRate.multiply(compoundFactor);
        BigDecimal denominator = compoundFactor.subtract(BigDecimal.ONE);
        BigDecimal paymentFactor = numerator.divide(denominator, DEFAULT_SCALE, RoundingMode.HALF_UP);
        
        return principal.multiply(paymentFactor).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 연수익률 계산
     * @param initialValue 초기 투자액
     * @param finalValue 최종 금액
     * @param years 투자 기간 (년)
     * @return 연 수익률
     */
    public static BigDecimal annualizedReturn(BigDecimal initialValue, BigDecimal finalValue, BigDecimal years) {
        if (initialValue == null || finalValue == null || years == null || 
            initialValue.compareTo(BigDecimal.ZERO) <= 0 || 
            finalValue.compareTo(BigDecimal.ZERO) <= 0 || 
            years.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // (Final/Initial)^(1/years) - 1
        double ratio = finalValue.divide(initialValue, DEFAULT_SCALE, RoundingMode.HALF_UP).doubleValue();
        double power = 1.0 / years.doubleValue();
        double result = Math.pow(ratio, power) - 1.0;
        
        return BigDecimal.valueOf(result).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 할인율을 이용한 현재가치 계산
     * 공식: PV = FV / (1 + r)^n
     * @param futureValue 미래가치
     * @param discountRate 할인율
     * @param periods 기간
     * @return 현재가치
     */
    public static BigDecimal presentValue(BigDecimal futureValue, BigDecimal discountRate, int periods) {
        if (futureValue == null || discountRate == null || periods < 0) {
            return BigDecimal.ZERO;
        }
        
        if (periods == 0) {
            return futureValue;
        }
        
        // (1 + r)^n 계산
        BigDecimal factor = BigDecimal.ONE.add(discountRate);
        BigDecimal discountFactor = BigDecimal.ONE;
        for (int i = 0; i < periods; i++) {
            discountFactor = discountFactor.multiply(factor);
        }
        
        return futureValue.divide(discountFactor, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 미래가치 계산
     * 공식: FV = PV * (1 + r)^n
     * @param presentValue 현재가치
     * @param interestRate 이자율
     * @param periods 기간
     * @return 미래가치
     */
    public static BigDecimal futureValue(BigDecimal presentValue, BigDecimal interestRate, int periods) {
        if (presentValue == null || interestRate == null || periods < 0) {
            return BigDecimal.ZERO;
        }
        
        if (periods == 0) {
            return presentValue;
        }
        
        // (1 + r)^n 계산
        BigDecimal factor = BigDecimal.ONE.add(interestRate);
        BigDecimal compoundFactor = BigDecimal.ONE;
        for (int i = 0; i < periods; i++) {
            compoundFactor = compoundFactor.multiply(factor);
        }
        
        return presentValue.multiply(compoundFactor).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 백분율을 소수로 변환
     * 예: 5.5% → 0.055
     */
    public static BigDecimal percentToDecimal(BigDecimal percent) {
        if (percent == null) return BigDecimal.ZERO;
        return percent.divide(BigDecimal.valueOf(100), DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 소수를 백분율로 변환
     * 예: 0.055 → 5.5%
     */
    public static BigDecimal decimalToPercent(BigDecimal decimal) {
        if (decimal == null) return BigDecimal.ZERO;
        return decimal.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 원화 형태로 반올림 (원 단위)
     */
    public static BigDecimal roundToWon(BigDecimal amount) {
        if (amount == null) return BigDecimal.ZERO;
        return amount.setScale(0, RoundingMode.HALF_UP);
    }
}