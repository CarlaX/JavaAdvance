package com.fzw.dubboprovider.mapper;

import com.fzw.dubbocommon.pojo.AssetFreezePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author fzw
 * @description
 * @date 2021-07-06
 **/
@Mapper
public interface AssetsFreezeMapper {

    public long addAssetsFreeze(@Param("assetFreezePO") AssetFreezePO assetFreezePO);

    public long removeAssetsFreeze(@Param("id") String id);

    public AssetFreezePO selectById(@Param("id") String id);

}
