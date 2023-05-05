package com.mu.paya.service;

/**
 * @author Mr.xin
 */
public interface PayService {
    String pay(String accountId, String orderId, String userId, double money);
}
