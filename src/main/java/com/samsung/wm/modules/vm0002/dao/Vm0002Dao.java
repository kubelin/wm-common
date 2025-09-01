package com.samsung.wm.modules.vm0002.dao;

import com.samsung.wm.modules.vm0002.dto.AccountDto;
import com.samsung.wm.modules.vm0002.dto.InquiryLogDto;
import com.samsung.wm.modules.vm0001.dto.CustomerDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * VM0002 계좌잔고 조회 DAO
 * vm0002.c의 데이터베이스 함수들을 MyBatis 인터페이스로 변환
 */
@Mapper
public interface Vm0002Dao {
    
    /**
     * 고객 존재 확인 (vm0002.c의 check_customer_exists 함수)
     * @param customerId 고객ID
     * @return 고객 정보 (존재하지 않으면 null)
     */
    @Select("SELECT customer_id as customerId, customer_name as customerName, " +
            "status, create_time as createTime FROM customer WHERE customer_id = #{customerId}")
    CustomerDto checkCustomer(@Param("customerId") String customerId);
    
    /**
     * 계좌 목록 조회 (vm0002.c의 select_account_list 함수)
     * @param customerId 고객ID
     * @param accountType 계좌유형 (선택사항)
     * @return 계좌 목록
     */
    @Select("<script>" +
            "SELECT account_no as accountNo, customer_id as customerId, " +
            "account_type as accountType, balance " +
            "FROM account WHERE customer_id = #{customerId} " +
            "<if test='accountType != null and accountType != \"\"'>" +
            "AND account_type = #{accountType}" +
            "</if>" +
            "</script>")
    List<AccountDto> selectAccountList(@Param("customerId") String customerId, 
                                      @Param("accountType") String accountType);
    
    /**
     * 조회 로그 기록 (vm0002.c의 insert_inquiry_log 함수)
     * @param inquiryLog 조회 로그 정보
     * @return 영향받은 행 수
     */
    @Insert("INSERT INTO inquiry_log (customer_id, inquiry_type, inquiry_time, account_count) " +
            "VALUES (#{customerId}, #{inquiryType}, #{inquiryTime}, #{accountCount})")
    int insertInquiryLog(InquiryLogDto inquiryLog);
    
}