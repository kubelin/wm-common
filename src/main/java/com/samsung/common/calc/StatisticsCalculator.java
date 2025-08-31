package com.samsung.common.calc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * 통계 계산 유틸리티
 * C의 통계 계산 함수들을 Java로 변환
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StatisticsCalculator {
    
    private static final int DEFAULT_SCALE = 4;
    
    /**
     * 평균값 계산
     * C의 평균 계산 함수와 유사
     */
    public static BigDecimal mean(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sum = values.stream()
            .filter(value -> value != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.divide(BigDecimal.valueOf(values.size()), DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 배열로 평균값 계산
     */
    public static BigDecimal mean(BigDecimal... values) {
        if (values == null || values.length == 0) {
            return BigDecimal.ZERO;
        }
        return mean(Arrays.asList(values));
    }
    
    /**
     * 중앙값 계산
     */
    public static BigDecimal median(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        List<BigDecimal> sorted = values.stream()
            .filter(value -> value != null)
            .sorted()
            .toList();
        
        int size = sorted.size();
        if (size % 2 == 1) {
            // 홀수 개인 경우 중간값
            return sorted.get(size / 2);
        } else {
            // 짝수 개인 경우 중간 두 값의 평균
            BigDecimal mid1 = sorted.get(size / 2 - 1);
            BigDecimal mid2 = sorted.get(size / 2);
            return mid1.add(mid2).divide(BigDecimal.valueOf(2), DEFAULT_SCALE, RoundingMode.HALF_UP);
        }
    }
    
    /**
     * 최대값 찾기
     */
    public static BigDecimal max(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return values.stream()
            .filter(value -> value != null)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }
    
    /**
     * 최소값 찾기
     */
    public static BigDecimal min(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return values.stream()
            .filter(value -> value != null)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }
    
    /**
     * 범위 계산 (최대값 - 최소값)
     */
    public static BigDecimal range(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal maxVal = max(values);
        BigDecimal minVal = min(values);
        
        return maxVal.subtract(minVal);
    }
    
    /**
     * 분산 계산 (모집단 분산)
     * C의 분산 계산 함수와 유사
     */
    public static BigDecimal variance(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal meanValue = mean(values);
        
        BigDecimal sumSquaredDifferences = values.stream()
            .filter(value -> value != null)
            .map(value -> {
                BigDecimal diff = value.subtract(meanValue);
                return diff.multiply(diff);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sumSquaredDifferences.divide(BigDecimal.valueOf(values.size()), DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 표본 분산 계산 (n-1로 나눔)
     */
    public static BigDecimal sampleVariance(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal meanValue = mean(values);
        
        BigDecimal sumSquaredDifferences = values.stream()
            .filter(value -> value != null)
            .map(value -> {
                BigDecimal diff = value.subtract(meanValue);
                return diff.multiply(diff);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sumSquaredDifferences.divide(BigDecimal.valueOf(values.size() - 1), DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 표준편차 계산 (모집단)
     */
    public static BigDecimal standardDeviation(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal varianceValue = variance(values);
        double sqrt = Math.sqrt(varianceValue.doubleValue());
        
        return BigDecimal.valueOf(sqrt).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 표본 표준편차 계산
     */
    public static BigDecimal sampleStandardDeviation(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal sampleVarianceValue = sampleVariance(values);
        double sqrt = Math.sqrt(sampleVarianceValue.doubleValue());
        
        return BigDecimal.valueOf(sqrt).setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 변동계수 계산 (표준편차 / 평균)
     * 데이터의 상대적 변동성을 나타냄
     */
    public static BigDecimal coefficientOfVariation(List<BigDecimal> values) {
        if (values == null || values.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal meanValue = mean(values);
        if (meanValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal stdDev = standardDeviation(values);
        return stdDev.divide(meanValue, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 백분위수 계산
     * @param values 데이터
     * @param percentile 백분위 (0-100)
     * @return 해당 백분위수 값
     */
    public static BigDecimal percentile(List<BigDecimal> values, int percentile) {
        if (values == null || values.isEmpty() || percentile < 0 || percentile > 100) {
            return BigDecimal.ZERO;
        }
        
        List<BigDecimal> sorted = values.stream()
            .filter(value -> value != null)
            .sorted()
            .toList();
        
        if (percentile == 0) {
            return sorted.get(0);
        }
        if (percentile == 100) {
            return sorted.get(sorted.size() - 1);
        }
        
        // 선형 보간을 사용한 백분위수 계산
        double index = (percentile / 100.0) * (sorted.size() - 1);
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);
        
        if (lowerIndex == upperIndex) {
            return sorted.get(lowerIndex);
        }
        
        BigDecimal lowerValue = sorted.get(lowerIndex);
        BigDecimal upperValue = sorted.get(upperIndex);
        BigDecimal weight = BigDecimal.valueOf(index - lowerIndex);
        
        return lowerValue.add(upperValue.subtract(lowerValue).multiply(weight))
                .setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
    
    /**
     * 사분위수 계산
     */
    public static class Quartiles {
        private final BigDecimal q1;
        private final BigDecimal q2;
        private final BigDecimal q3;
        
        public Quartiles(BigDecimal q1, BigDecimal q2, BigDecimal q3) {
            this.q1 = q1;
            this.q2 = q2;
            this.q3 = q3;
        }
        
        public BigDecimal getQ1() { return q1; }
        public BigDecimal getQ2() { return q2; }
        public BigDecimal getQ3() { return q3; }
        public BigDecimal getIQR() { return q3.subtract(q1); }
    }
    
    public static Quartiles quartiles(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            return new Quartiles(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        
        BigDecimal q1 = percentile(values, 25);
        BigDecimal q2 = percentile(values, 50);  // 중앙값
        BigDecimal q3 = percentile(values, 75);
        
        return new Quartiles(q1, q2, q3);
    }
    
    /**
     * 상관계수 계산 (피어슨 상관계수)
     * 두 변수 간의 선형 상관관계를 나타냄
     */
    public static BigDecimal correlation(List<BigDecimal> x, List<BigDecimal> y) {
        if (x == null || y == null || x.size() != y.size() || x.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal meanX = mean(x);
        BigDecimal meanY = mean(y);
        
        BigDecimal numerator = BigDecimal.ZERO;
        BigDecimal sumSquaredX = BigDecimal.ZERO;
        BigDecimal sumSquaredY = BigDecimal.ZERO;
        
        for (int i = 0; i < x.size(); i++) {
            BigDecimal diffX = x.get(i).subtract(meanX);
            BigDecimal diffY = y.get(i).subtract(meanY);
            
            numerator = numerator.add(diffX.multiply(diffY));
            sumSquaredX = sumSquaredX.add(diffX.multiply(diffX));
            sumSquaredY = sumSquaredY.add(diffY.multiply(diffY));
        }
        
        BigDecimal denominator = sumSquaredX.multiply(sumSquaredY);
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        double sqrt = Math.sqrt(denominator.doubleValue());
        BigDecimal sqrtBD = BigDecimal.valueOf(sqrt);
        
        return numerator.divide(sqrtBD, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
}