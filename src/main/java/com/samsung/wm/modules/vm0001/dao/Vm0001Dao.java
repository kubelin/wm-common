package com.samsung.wm.modules.vm0001.dao;

import com.samsung.wm.modules.vm0001.dto.AccessLogDto;
import com.samsung.wm.modules.vm0001.dto.CustomerDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * VM0001 고객정보 조회 DAO
 * vm0001.c의 데이터베이스 함수들을 MyBatis 인터페이스로 변환
 */
@Mapper
public interface Vm0001Dao {
    
    /**
     * 고객 정보 조회 (vm0001.c의 select_customer 함수)
     * @param customerId 고객ID
     * @return 고객 정보
     */
    @Select("SELECT customer_id as customerId, customer_name as customerName, " +
            "status, create_time as createTime, last_access_time as lastAccessTime " +
            "FROM customer WHERE customer_id = #{customerId}")
    CustomerDto selectCustomer(@Param("customerId") String customerId);
    
    /**
     * 접근 로그 기록 (vm0001.c의 insert_access_log 함수)
     * @param accessLog 접근 로그 정보
     * @return 영향받은 행 수
     */
    @Insert("INSERT INTO access_log (customer_id, access_type, access_time) " +
            "VALUES (#{customerId}, #{accessType}, #{accessTime})")
    int insertAccessLog(AccessLogDto accessLog);
    
    /**
     * 마지막 접근 시간 업데이트 (vm0001.c의 update_last_access 함수)
     * @param customerId 고객ID
     * @param lastAccessTime 마지막 접근 시간
     * @return 영향받은 행 수
     */
    @Update("UPDATE customer SET last_access_time = #{lastAccessTime} WHERE customer_id = #{customerId}")
    int updateLastAccess(@Param("customerId") String customerId, 
                        @Param("lastAccessTime") java.time.LocalDateTime lastAccessTime);
    
}