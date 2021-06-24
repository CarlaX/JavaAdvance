package com.fzw.shardingspherespringdemo.mapper;

import com.fzw.shardingspherespringdemo.pojo.po.UserPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author fzw
 * @description
 * @date 2021-06-24
 **/
@Mapper
public interface UserMapper {
    public List<UserPO> queryAllUser();
}
