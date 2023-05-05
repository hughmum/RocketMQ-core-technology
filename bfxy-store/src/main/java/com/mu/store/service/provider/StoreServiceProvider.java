package com.mu.store.service.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.mu.store.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

/**
 * @author Mr.xin
 */

@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}"
)
public class StoreServiceProvider implements StoreServiceApi{
    @Autowired
    private StoreMapper storeMapper;

    @Override

    public int selectStoreCount(String supplierId, String goodsId) {
        System.out.println("====== selectStoreCount ======");
        return storeMapper.selectStoreCount(supplierId,goodsId);
    }

    @Override
    public int selectVersion(String supplierId, String goodsId) {
        System.out.println("====== selectVersion ======");
        return storeMapper.selectVersion(supplierId,goodsId);
    }

    @Override
    public int updateStoreCountByVersion(String supplierId, String goodsId, Integer version, Date updateTime, String updateBy) {
        System.out.println("====== updateStoreCountByVersion ======");
        return storeMapper.updateStoreCountByVersion(version,supplierId,goodsId,updateBy,updateTime);
    }
}
