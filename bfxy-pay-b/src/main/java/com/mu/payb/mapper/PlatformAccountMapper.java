package com.mu.payb.mapper;

import com.mu.payb.entity.PlatformAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlatformAccountMapper {
    int deleteByPrimaryKey(String accountId);

    int insert(PlatformAccount record);

    int insertSelective(PlatformAccount record);

    PlatformAccount selectByPrimaryKey(String accountId);

    int updateByPrimaryKeySelective(PlatformAccount record);

    int updateByPrimaryKey(PlatformAccount record);
}