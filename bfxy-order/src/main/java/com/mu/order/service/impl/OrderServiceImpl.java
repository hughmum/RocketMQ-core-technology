package com.mu.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mu.order.constants.OrderStatus;
import com.mu.order.entity.Order;
import com.mu.order.mapper.OrderMapper;
import com.mu.order.service.OrderService;
import com.mu.order.service.producer.OrderlyProducer;
import com.mu.order.utils.FastJsonConvertUtil;
import com.mu.store.service.provider.StoreServiceApi;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

/**
 * @author Mr.xin
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Reference(
            version = "1.0.0",
            interfaceName = "com.mu.store.service.provider.StoreServiceApi",
            application = "${dubbo.application.id}",
            check = false,
            retries = 0,
            timeout = 3000
    )
    private StoreServiceApi storeServiceApi;

    @Autowired
    private OrderlyProducer orderlyProducer;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean createOrder(String cityId, String platformId, String userId, String supplierId, String goodsId) {
        boolean flag = true;
        Order order = new Order();
        String orderId = UUID.randomUUID().toString().replace("-", "");
        order.setOrderId(orderId);
        order.setCityId(cityId);
        order.setPlatformId(platformId);
        order.setUserId(userId);
        order.setSupplierId(supplierId);
        order.setGoodsId(goodsId);
        order.setCreateBy("admin");
        order.setOrderStatus(OrderStatus.CREATED.getCode());
        order.setOrderType("0");
        order.setCreateTime(new Date(System.currentTimeMillis()));
        order.setCreateBy("admin");
        order.setRemark("");
        order.setUpdateBy("admin");
        order.setUpdateTime(new Date(System.currentTimeMillis()));

        try {
            int storeCount = storeServiceApi.selectStoreCount(supplierId, goodsId);
            if (storeCount < 0) {
                return false;
            }
            int version = storeServiceApi.selectVersion(supplierId, goodsId);
            //这里乐观锁
            int updateRetCount = storeServiceApi.updateStoreCountByVersion(supplierId, goodsId, version, new Date(System.currentTimeMillis()), "admin");
            if (updateRetCount == 1) {
                orderMapper.insertSelective(order);
            } else if (updateRetCount == 0) {
                flag = false;
                int currentCount = storeServiceApi.selectStoreCount(supplierId, goodsId);
                if (currentCount == 0) {
                    System.out.println("库存不足");
                } else {
                    System.out.println("乐观锁生效");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            // 回滚操作
            orderMapper.deleteByPrimaryKey(orderId);
        }

        return flag;
    }

    public static final String PKG_TOPIC = "pkg_topic";

    public static final String PKG_TAGS = "pkg";

    @Override
    public void sendOrderlyMessage4Pkg(String userId, String orderId) {
        List<Message> messageList = new ArrayList<>();

        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("userId", userId);
        param1.put("orderId", orderId);
        param1.put("text", "创建包裹操作---1");

        String key1 = UUID.randomUUID().toString() + "$" + System.currentTimeMillis();
        Message message1 = new Message(PKG_TOPIC, PKG_TAGS, key1, FastJsonConvertUtil.convertObjectToJSON(param1).getBytes());

        messageList.add(message1);


        Map<String, Object> param2 = new HashMap<String, Object>();
        param2.put("userId", userId);
        param2.put("orderId", orderId);
        param2.put("text", "发送物流通知操作---2");

        String key2 = UUID.randomUUID().toString() + "$" + System.currentTimeMillis();
        Message message2 = new Message(PKG_TOPIC, PKG_TAGS, key2, FastJsonConvertUtil.convertObjectToJSON(param2).getBytes());

        messageList.add(message2);

        //	顺序消息投递 是应该按照 供应商ID 与topic 和 messagequeueId 进行绑定对应的
        //  supplier_id

        Order order = orderMapper.selectByPrimaryKey(orderId);
        int messageQueueNumber = Integer.parseInt(order.getSupplierId());

        //对应的顺序消息的生产者 把messageList 发出去
        orderlyProducer.sendOrderlyMessages(messageList, messageQueueNumber);
    }
}
