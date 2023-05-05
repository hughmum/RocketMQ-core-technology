package com.mu.paya.service.provider;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class TransactionMessageProducer implements InitializingBean { //这里使用到InitializingBean
    private TransactionMQProducer transactionMQProducer;
    private static final String TRANSACTION_PRODUCER_GROUP = "TPG";
    private static final String NAMESERVER = "101.42.13.83:9876";


    @Autowired
    private TransactionListenerImpl transactionListener;

    public TransactionMessageProducer() throws MQClientException {
        this.transactionMQProducer = new TransactionMQProducer(TRANSACTION_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(NAMESERVER);
        transactionMQProducer.setRetryTimesWhenSendFailed(3);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS
                , new ArrayBlockingQueue<Runnable>(2000)
                , new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("transaction-producer-thread");
                return thread;
            }
        });
        transactionMQProducer.setExecutorService(threadPoolExecutor);
    }
    public TransactionSendResult send(Message message, Object args) throws MQClientException {
       return transactionMQProducer.sendMessageInTransaction(message, args);
    }

    //等项目初始化完成后，进行一个注入listener
    @Override
    public void afterPropertiesSet() throws Exception {
        transactionMQProducer.setTransactionListener(transactionListener);
        start();
        System.out.println("tx_producer启动成功");

    }

    private void start() throws MQClientException {
        assert transactionMQProducer != null;
        transactionMQProducer.start();
    }
}
