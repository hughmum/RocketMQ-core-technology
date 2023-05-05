package com.mu.paya.service.impl;

import com.mu.paya.entity.CustomerAccount;
import com.mu.paya.mapper.CustomerAccountMapper;
import com.mu.paya.service.PayService;
import com.mu.paya.service.provider.CallBackService;
import com.mu.paya.service.provider.TransactionMessageProducer;
import com.mu.paya.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mr.xin
 */
@Service
public class PayServiceImpl implements PayService {

    public static final String TX_PAY_TOPIC = "tx_pay_topic";
    public static final String TX_PAY_TAGS = "pay";

    @Autowired
    private CustomerAccountMapper customerAccountMapper;

    @Autowired
    private TransactionMessageProducer transactionMessageProducer;


    @Autowired
    private CallBackService callbackService;

    @Override
    public String pay(String accountId, String orderId, String userId, double money) {
        System.out.println(Thread.currentThread().getName());
        String resultMsg = null;
        // token 支付去重
        // 分布式锁保证在同一时刻只有一个线程在操作数据库
        //lock
        CustomerAccount old = customerAccountMapper.selectByPrimaryKey(accountId);
        Integer version = old.getVersion();
        BigDecimal currentBalance = old.getCurrentBalance();
        BigDecimal newBalance = currentBalance.subtract(new BigDecimal(money));
        if (newBalance.doubleValue() < 0) {
            resultMsg = "余额不足";
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("accountId", accountId);
            data.put("orderId", orderId);
            data.put("userId", userId);
            data.put("money", money);

            String messageKey = UUID.randomUUID().toString().replaceAll("-", "");
            Message message = new Message(TX_PAY_TOPIC, TX_PAY_TAGS, messageKey, FastJsonConvertUtil.convertObjectToJSON(data).getBytes());
            try {
                CountDownLatch countDownLatch = new CountDownLatch(1);  //在这个例子中，CountDownLatch 被用作一个同步器，用于等待事务消息的执行结果返回
                Map<String, Object> args = new HashMap<>();
                args.put("version", version);
                args.put("newBalance", newBalance);
                args.put("accountId", accountId);
                args.put("countDownLatch", countDownLatch);
                TransactionSendResult sendResult = transactionMessageProducer.send(message, args);
                countDownLatch.await();
                //transactionMessageProducer.send 方法。这个方法将会启动一个本地事务，执行完本地事务后，会向 RocketMQ 发送一个事务确认消息。在发送事务确认消息前，代码调用了 countDownLatch.await() 方法，阻塞当前线程，等待事务执行结果返回。
                //
                //在事务执行结果返回后，代码调用 countDownLatch.countDown() 方法，将 CountDownLatch 计数器减 1。此时，countDownLatch.await() 方法会返回，当前线程恢复执行，可以根据事务执行结果进行下一步处理。
                System.out.println(sendResult.getLocalTransactionState());
                if (sendResult.getSendStatus() == SendStatus.SEND_OK
                        && sendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
                    // 修改订单状态
                    SendResult result = callbackService.sendOKMessage(userId, orderId);
                    System.out.println(result);

                    resultMsg = "支付成功";
                } else {
                    resultMsg = "支付失败";
                }
            } catch (MQClientException | InterruptedException e) {
                e.printStackTrace();
                resultMsg = "支付失败";
            }
        }

        return resultMsg;
    }
}
