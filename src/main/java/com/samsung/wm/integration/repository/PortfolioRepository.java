package com.samsung.wm.integration.repository;

import com.samsung.wm.strategy.portfolio.Portfolio;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 포트폴리오 저장소 인터페이스
 */
@Repository
public interface PortfolioRepository {
    
    /**
     * 포트폴리오 ID로 조회
     * 
     * @param portfolioId 포트폴리오 ID
     * @return 포트폴리오 정보
     */
    Optional<Portfolio> findById(String portfolioId);
    
    /**
     * 고객별 포트폴리오 목록 조회
     * 
     * @param customerId 고객 ID
     * @return 포트폴리오 목록
     */
    List<Portfolio> findByCustomerId(String customerId);
    
    /**
     * 활성 포트폴리오 목록 조회
     * 
     * @return 활성 포트폴리오 목록
     */
    List<Portfolio> findByStatus(String status);
    
    /**
     * 포트폴리오 저장
     * 
     * @param portfolio 포트폴리오 정보
     * @return 저장된 포트폴리오
     */
    Portfolio save(Portfolio portfolio);
    
    /**
     * 포트폴리오 삭제
     * 
     * @param portfolioId 포트폴리오 ID
     */
    void deleteById(String portfolioId);
}