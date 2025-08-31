package com.samsung.wm.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * 포트폴리오 계산 유틸리티 클래스
 */
public final class PortfolioCalculationUtil {
    
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
    
    private PortfolioCalculationUtil() {
        // 인스턴스 생성 방지
    }
    
    /**
     * 포트폴리오 총 가치 계산
     * 
     * @param holdings 보유 종목 목록
     * @return 총 가치
     */
    public static BigDecimal calculateTotalValue(Map<String, HoldingInfo> holdings) {
        return holdings.values().stream()
            .map(holding -> holding.getQuantity().multiply(holding.getCurrentPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * 수익률 계산
     * 
     * @param currentValue 현재 가치
     * @param initialValue 초기 투자 금액
     * @return 수익률 (%)
     */
    public static Double calculateReturnRate(BigDecimal currentValue, BigDecimal initialValue) {
        if (initialValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        BigDecimal returnAmount = currentValue.subtract(initialValue);
        BigDecimal returnRate = returnAmount.divide(initialValue, MATH_CONTEXT)
                                          .multiply(new BigDecimal("100"));
        
        return returnRate.doubleValue();
    }
    
    /**
     * 자산 비중 계산
     * 
     * @param assetValue 자산 가치
     * @param totalValue 총 포트폴리오 가치
     * @return 비중 (%)
     */
    public static Double calculateAssetWeight(BigDecimal assetValue, BigDecimal totalValue) {
        if (totalValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        
        BigDecimal weight = assetValue.divide(totalValue, MATH_CONTEXT)
                                    .multiply(new BigDecimal("100"));
        
        return weight.doubleValue();
    }
    
    /**
     * 리밸런싱 필요 여부 확인
     * 
     * @param currentWeights 현재 비중
     * @param targetWeights 목표 비중
     * @param threshold 임계값
     * @return 리밸런싱 필요 여부
     */
    public static boolean needsRebalancing(Map<String, Double> currentWeights, 
                                         Map<String, Double> targetWeights, 
                                         double threshold) {
        for (String assetType : targetWeights.keySet()) {
            double currentWeight = currentWeights.getOrDefault(assetType, 0.0);
            double targetWeight = targetWeights.get(assetType);
            double deviation = Math.abs(currentWeight - targetWeight);
            
            if (deviation > threshold) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 샤프 비율 계산
     * 
     * @param portfolioReturn 포트폴리오 수익률
     * @param riskFreeRate 무위험 수익률
     * @param volatility 변동성
     * @return 샤프 비율
     */
    public static Double calculateSharpeRatio(double portfolioReturn, 
                                            double riskFreeRate, 
                                            double volatility) {
        if (volatility == 0) {
            return 0.0;
        }
        
        return (portfolioReturn - riskFreeRate) / volatility;
    }
    
    /**
     * 보유 종목 정보 내부 클래스
     */
    public static class HoldingInfo {
        private final BigDecimal quantity;
        private final BigDecimal currentPrice;
        private final BigDecimal avgPrice;
        
        public HoldingInfo(BigDecimal quantity, BigDecimal currentPrice, BigDecimal avgPrice) {
            this.quantity = quantity;
            this.currentPrice = currentPrice;
            this.avgPrice = avgPrice;
        }
        
        public BigDecimal getQuantity() { return quantity; }
        public BigDecimal getCurrentPrice() { return currentPrice; }
        public BigDecimal getAvgPrice() { return avgPrice; }
    }
}