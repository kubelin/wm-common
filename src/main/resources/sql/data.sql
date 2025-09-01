-- VM Common 모듈 테스트 데이터

-- 고객 데이터
INSERT INTO customer (customer_id, customer_name, status, create_time) VALUES 
('CUST001', '홍길동', 'ACTIVE', '2025-01-01'),
('CUST002', '김철수', 'ACTIVE', '2025-01-02'),
('CUST003', '박영희', 'INACTIVE', '2025-01-03');

-- 계좌 데이터  
INSERT INTO account (account_no, customer_id, account_type, balance) VALUES
('1001-001-001', 'CUST001', 'SAVINGS', 1000000.00),
('1001-001-002', 'CUST001', 'CHECKING', 500000.00),
('2001-002-001', 'CUST002', 'SAVINGS', 2000000.00),
('2001-002-002', 'CUST002', 'INVESTMENT', 1500000.00),
('3001-003-001', 'CUST003', 'SAVINGS', 300000.00);