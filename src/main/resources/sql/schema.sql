-- VM Common 모듈 데이터베이스 스키마
-- C 파일의 구조체들을 테이블로 변환

-- 고객 테이블 (customer_info_t)
CREATE TABLE IF NOT EXISTS customer (
    customer_id VARCHAR(20) PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    create_time VARCHAR(20) NOT NULL,
    last_access_time TIMESTAMP
);

-- 계좌 테이블 (account_info_t)  
CREATE TABLE IF NOT EXISTS account (
    account_no VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- 접근 로그 테이블 (access_log_t)
CREATE TABLE IF NOT EXISTS access_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    access_type VARCHAR(20) NOT NULL,
    access_time TIMESTAMP NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- 잔고 조회 로그 테이블 (inquiry_log_t)
CREATE TABLE IF NOT EXISTS inquiry_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    inquiry_type VARCHAR(20) NOT NULL,
    inquiry_time TIMESTAMP NOT NULL,
    account_count INTEGER DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);