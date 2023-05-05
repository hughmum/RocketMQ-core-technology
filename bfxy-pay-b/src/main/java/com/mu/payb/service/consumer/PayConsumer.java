package com.mu.payb.service.consumer;

import com.mu.payb.entity.PlatformAccount;
import com.mu.payb.mapper.PlatformAccountMapper;
import com.mu.payb.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PayConsumer {


    private DefaultMQPushConsumer consumer;
    private static final String NAMESERVER = "101.42.13.83:9876";

    private static final String CONSUMER_GROUP_NAME = "tx_pay_consumer_group_name";

    public static final String TX_PAY_TOPIC = "tx_pay_topic";

    public static final String TX_PAY_TAGS = "pay";


    @Autowired
    private PlatformAccountMapper platformAccountMapper;


    private PayConsumer() {
        try {
            consumer = new DefaultMQPushConsumer(CONSUMER_GROUP_NAME);
            consumer.setNamesrvAddr(NAMESERVER);
            consumer.setConsumeThreadMin(10);
            consumer.setConsumeThreadMax(30);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe(TX_PAY_TOPIC, TX_PAY_TAGS);
            consumer.registerMessageListener(new MessageListenerConcurrently4Pay());
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }

    private  class MessageListenerConcurrently4Pay implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            MessageExt messageExt = list.get(0);
            String topic = messageExt.getTopic();
            String keys = messageExt.getKeys();
            String tags = messageExt.getTags();
            System.out.println("topic=======>" + topic);
            System.out.println("keys ======>" + keys);
            System.out.println("tags ======>" + tags);
            try {
                String body = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                Map<String, Object> paramsBody = FastJsonConvertUtil.convertJSONToObject(body, Map.class);
                String userId = (String)paramsBody.get("userId");	// customer userId
                String accountId = (String)paramsBody.get("accountId");	//customer accountId
                String orderId = (String)paramsBody.get("orderId");	// 	统一的订单
                BigDecimal money = (BigDecimal)paramsBody.get("money");	//	当前的收益款

                PlatformAccount pa = platformAccountMapper.selectByPrimaryKey("platform001");	//	当前平台的一个账号

                pa.setCurrentBalance(pa.getCurrentBalance().add(money));
                Date currentTime = new Date();
                pa.setVersion(pa.getVersion() + 1);
                pa.setDateTime(currentTime);
                pa.setUpdateTime(currentTime);
                System.out.println(pa);
                platformAccountMapper.updateByPrimaryKeySelective(pa);


            } catch (Exception e) {
                e.printStackTrace();
                // 统计次数，超过次数后人工补偿
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }


    }
}
