package com.mu.store.service.provider;

import java.sql.Date;

/**
 * @author Mr.xin
 */
public interface StoreServiceApi {
    int selectStoreCount(String supplierId, String goodsId);
    int selectVersion(String supplierId, String goodsId);
    int updateStoreCountByVersion(String supplierId, String goodsId, Integer version, Date updateTime, String updateBy);
}
