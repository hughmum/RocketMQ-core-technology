package com.mu.order.service.consumer;

import com.mu.order.constants.OrderStatus;
import com.mu.order.mapper.OrderMapper;
import com.mu.order.service.OrderService;
import com.mu.order.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class OrderConsumer {


    public static final String CALL_BACK_TOPIC = "callback_pay_topic";
    public static final String CALL_BACK_TAG = "callback_pay";
    public static final String NAMESERVER = "101.43.133.162:9876";

    private DefaultMQPushConsumer consumer;

    @Autowired
    private OrderService orderService;
    private OrderConsumer(OrderMapper orderMapper) throws MQClientException {
        this.consumer = new DefaultMQPushConsumer("call_back_consume_group");
        consumer.setNamesrvAddr(NAMESERVER);
        this.consumer.setConsumeThreadMin(10);
        this.consumer.setConsumeThreadMax(30);
        this.consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        this.consumer.subscribe(CALL_BACK_TOPIC,CALL_BACK_TAG);
        this.consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                String topic = msg.getTopic();
                String tags = msg.getTags();
                String keys = msg.getKeys();
                System.out.println("topic ======>" + topic);
                System.out.println("tags ======>" + tags);
                System.out.println("keys ======>" + keys);
                //可以通过keys做消息去重
                String data = new String(msg.getBody());
                Map<String, Object> body = FastJsonConvertUtil.convertJSONToObject(data, Map.class);
                String orderId = (String) body.get("orderId");
                String userId = (String) body.get("userId");
                String status = (String)body.get("status");

                Date currentTime = new Date();

                if(status.equals(OrderStatus.SUCCESS.getCode())) {
                    int count  = orderMapper.updateOrderStatus(orderId, status, "admin", currentTime);
                    if(count == 1) {
                        orderService.sendOrderlyMessage4Pkg(userId, orderId);
                    }

                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        this.consumer.start();


    }
}
