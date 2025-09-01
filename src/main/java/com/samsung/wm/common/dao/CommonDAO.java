package com.samsung.wm.common.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 공통 DAO 클래스
 * 모든 모듈의 DB 처리를 담당하는 범용 DAO
 * 
 * 실제 프로젝트에서는 MyBatis나 JPA를 사용하지만, 
 * 데모용으로 메모리 기반 구현 제공
 */
@Slf4j
@Repository
public class CommonDAO {
    
    // 데모용 메모리 저장소
    private final Map<String, List<Map<String, Object>>> memoryStore = new HashMap<>();
    
    public CommonDAO() {
        initializeSampleData();
    }
    
    /**
     * 단일 결과 조회
     */
    public <T> T selectOne(String sqlId, Object params, Class<T> type) {
        log.info("Executing query: {} with params: {}", sqlId, params);
        
        // 실제로는 MyBatis sqlSession.selectOne(sqlId, params) 사용
        List<Map<String, Object>> results = selectMapList(sqlId, params);
        if (results.isEmpty()) {
            return null;
        }
        
        return convertToType(results.get(0), type);
    }
    
    /**
     * 목록 결과 조회  
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectList(String sqlId, Object params, Class<T> type) {
        log.info("Executing query list: {} with params: {}", sqlId, params);
        
        // 데모용 구현 - 실제로는 MyBatis sqlSession.selectList(sqlId, params) 사용
        List<Map<String, Object>> data = memoryStore.getOrDefault(getTableName(sqlId), new ArrayList<>());
        
        if (type == Map.class) {
            return (List<T>) data;
        }
        
        return data.stream()
                   .map(row -> convertToType(row, type))
                   .toList();
    }
    
    /**
     * Map 목록 조회 (타입 안전성 보장)
     */
    public List<Map<String, Object>> selectMapList(String sqlId, Object params) {
        log.info("Executing map list query: {} with params: {}", sqlId, params);
        return memoryStore.getOrDefault(getTableName(sqlId), new ArrayList<>());
    }
    
    /**
     * 데이터 삽입
     */
    public int insert(String sqlId, Object params) {
        log.info("Executing insert: {} with params: {}", sqlId, params);
        
        // 실제로는 MyBatis sqlSession.insert(sqlId, params) 사용
        String tableName = getTableName(sqlId);
        List<Map<String, Object>> data = memoryStore.computeIfAbsent(tableName, k -> new ArrayList<>());
        
        if (params instanceof Map) {
            data.add(new HashMap<>((Map<String, Object>) params));
            return 1;
        }
        
        return 0;
    }
    
    /**
     * 데이터 수정
     */
    public int update(String sqlId, Object params) {
        log.info("Executing update: {} with params: {}", sqlId, params);
        
        // 실제로는 MyBatis sqlSession.update(sqlId, params) 사용
        // 데모용으로 간단하게 구현
        return 1;
    }
    
    /**
     * 데이터 삭제
     */
    public int delete(String sqlId, Object params) {
        log.info("Executing delete: {} with params: {}", sqlId, params);
        
        // 실제로는 MyBatis sqlSession.delete(sqlId, params) 사용
        return 1;
    }
    
    // === Private Helper Methods ===
    
    private String getTableName(String sqlId) {
        // sqlId에서 테이블명 추출 (예: "vm0001.selectCustomer" -> "customer")
        if (sqlId.contains("Customer")) return "customer";
        if (sqlId.contains("Account")) return "account";
        if (sqlId.contains("Log")) return "access_log";
        return "default";
    }
    
    @SuppressWarnings("unchecked")
    private <T> T convertToType(Map<String, Object> data, Class<T> type) {
        if (type == Map.class) {
            return (T) data;
        }
        
        // 실제로는 MyBatis TypeHandler나 Jackson 사용
        // 데모용 간단 변환
        if (type == String.class) {
            return (T) data.toString();
        }
        
        return (T) data;
    }
    
    private void initializeSampleData() {
        // 데모용 샘플 데이터
        List<Map<String, Object>> customers = new ArrayList<>();
        customers.add(Map.of(
            "customerId", "CUST001",
            "customerName", "홍길동",
            "status", "ACTIVE",
            "createTime", "2025-01-01"
        ));
        customers.add(Map.of(
            "customerId", "CUST002", 
            "customerName", "김철수",
            "status", "ACTIVE",
            "createTime", "2025-01-02"
        ));
        memoryStore.put("customer", customers);
        
        List<Map<String, Object>> accounts = new ArrayList<>();
        accounts.add(Map.of(
            "accountNo", "1001-001-001",
            "customerId", "CUST001",
            "balance", 1000000,
            "accountType", "SAVINGS"
        ));
        memoryStore.put("account", accounts);
    }
}