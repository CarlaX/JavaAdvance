package com.fzw.dubboprovider.mapper;

import com.fzw.dubbocommon.pojo.UserDollarAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@Mapper
public interface DollarAccountMapper {

    public long plusDollarAccount(@Param("id") String id, @Param("amount") BigDecimal amount);

    public long minusDollarAccount(@Param("id") String id, @Param("amount") BigDecimal amount);

    public UserDollarAccountPO selectById(@Param("id") String id);

}
