package com.mu.paya.service.provider;

import com.mu.paya.constants.OrderStatus;
import com.mu.paya.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Mr.xin
 */

@Component
public class CallBackService {
    public static final String CALL_BACK_TOPIC = "callback_pay_topic";
    public static final String CALL_BACK_TAG = "callback_pay";
    public static final String NAMESRV_ADDR = "101.42.13.83:9876";
    @Autowired
    private SyncProducer syncProducer;

    public SendResult sendOKMessage(String userId, String orderId){
        Map<String, Object> params = new HashMap<>();
        String messageKey = UUID.randomUUID().toString().replaceAll("-", "");
        params.put("userId",userId);
        params.put("orderId", orderId);
        params.put("status", OrderStatus.SUCCESS.getCode());

        Message message = new Message(CALL_BACK_TOPIC, CALL_BACK_TAG,messageKey,
                FastJsonConvertUtil.convertObjectToJSON(params).getBytes());

        return syncProducer.sendMessage(message);
    }


}
