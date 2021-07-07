package com.fzw.dubboprovider.mapper;

import com.fzw.dubbocommon.pojo.UserDollarAccountPO;
import com.fzw.dubbocommon.pojo.UserRmbAccountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@Mapper
public interface RmbAccountMapper {
    public long plusRmbAccount(@Param("id") String id, @Param("amount") BigDecimal amount);

    public long minusRmbAccount(@Param("id") String id, @Param("amount") BigDecimal amount);

    public UserRmbAccountPO selectById(@Param("id") String id);
}
