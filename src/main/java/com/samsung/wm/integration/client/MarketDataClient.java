package com.samsung.wm.integration.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 시장 데이터 조회 클라이언트
 */
@Slf4j
@Component
public class MarketDataClient {
    
    /**
     * 실시간 주식 가격 조회
     * 
     * @param symbol 종목 코드
     * @return 주식 가격 정보
     */
    public StockPrice getStockPrice(String symbol) {
        log.info("주식 가격 조회 요청 - symbol: {}", symbol);
        
        // 실제로는 외부 API 호출
        return new StockPrice(
            symbol,
            new BigDecimal("150000"),
            new BigDecimal("148000"),
            new BigDecimal("152000"),
            new BigDecimal("149500"),
            1000000L
        );
    }
    
    /**
     * 여러 종목 가격 일괄 조회
     * 
     * @param symbols 종목 코드 배열
     * @return 종목별 가격 정보 맵
     */
    public Map<String, StockPrice> getMultipleStockPrices(String[] symbols) {
        log.info("여러 종목 가격 조회 - count: {}", symbols.length);
        
        // 실제로는 외부 API 호출하여 일괄 조회
        return Map.of(); // 샘플 구현
    }
    
    /**
     * 시장 지수 조회
     * 
     * @param indexName 지수명 (KOSPI, KOSDAQ, S&P500 등)
     * @return 시장 지수 정보
     */
    public MarketIndex getMarketIndex(String indexName) {
        log.info("시장 지수 조회 - indexName: {}", indexName);
        
        return new MarketIndex(
            indexName,
            new BigDecimal("2500.50"),
            new BigDecimal("15.30"),
            0.62
        );
    }
}