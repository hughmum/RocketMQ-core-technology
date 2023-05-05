package com.mu.paya.service.provider;

import com.mu.paya.mapper.CustomerAccountMapper;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mr.xin
 */
@Component("TransactionListener")
public class TransactionListenerImpl implements TransactionListener {




    @Autowired
    private CustomerAccountMapper customerAccountMapper;


    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("执行本地事务");
        Map<String, Object> args = (Map<String, Object>) o;
        int version = (int) args.get("version");
        String accountId = (String) args.get("accountId");
        BigDecimal newBalance = (BigDecimal) args.get("newBalance");
        CountDownLatch countDownLatch = (CountDownLatch)args.get("countDownLatch");
        System.out.println(Thread.currentThread().getName());
        try {

            int updateRowCount = customerAccountMapper.updateBalance(accountId, newBalance, version, new Date(System.currentTimeMillis()));
            if(updateRowCount == 1){
                countDownLatch.countDown();
                return LocalTransactionState.COMMIT_MESSAGE;
            }else{
                countDownLatch.countDown();
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }catch (Exception e){
            e.printStackTrace();
            countDownLatch.countDown();
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        return null;
    }
}
