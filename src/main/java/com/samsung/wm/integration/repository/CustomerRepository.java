package com.samsung.wm.integration.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 고객 정보 저장소 인터페이스
 */
@Repository
public interface CustomerRepository {
    
    /**
     * 고객 ID로 고객 정보 조회
     * 
     * @param customerId 고객 ID
     * @return 고객 정보
     */
    Optional<Customer> findById(String customerId);
    
    /**
     * 고객명으로 고객 검색
     * 
     * @param name 고객명
     * @return 검색된 고객 목록
     */
    List<Customer> findByNameContaining(String name);
    
    /**
     * 위험성향별 고객 조회
     * 
     * @param riskProfile 위험성향
     * @return 고객 목록
     */
    List<Customer> findByRiskProfile(String riskProfile);
    
    /**
     * 고객 정보 저장
     * 
     * @param customer 고객 정보
     * @return 저장된 고객 정보
     */
    Customer save(Customer customer);
    
    /**
     * 고객 정보 삭제
     * 
     * @param customerId 고객 ID
     */
    void deleteById(String customerId);
}