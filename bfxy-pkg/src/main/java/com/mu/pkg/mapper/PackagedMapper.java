package com.mu.pkg.mapper;

import com.mu.pkg.entity.Packaged;

public interface PackagedMapper {
    int deleteByPrimaryKey(String packageId);

    int insert(Packaged record);

    int insertSelective(Packaged record);

    Packaged selectByPrimaryKey(String packageId);

    int updateByPrimaryKeySelective(Packaged record);

    int updateByPrimaryKey(Packaged record);
}