package com.mu.paya.mapper;

import com.mu.paya.entity.BrokerMessageLog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BrokerMessageLogMapper {
    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessageLog record);

    int insertSelective(BrokerMessageLog record);

    BrokerMessageLog selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessageLog record);

    int updateByPrimaryKey(BrokerMessageLog record);
}