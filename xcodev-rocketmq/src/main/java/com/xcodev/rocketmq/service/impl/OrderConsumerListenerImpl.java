package com.xcodev.rocketmq.service.impl;

import com.xcodev.rocketmq.entity.Order;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author xcodev
 */
@Component
@RocketMQMessageListener(consumerGroup = "order_consumer_group", topic = "xcodev_message")
public class OrderConsumerListenerImpl implements RocketMQListener<Order> {
    @Override
    public void onMessage(Order message) {
        System.out.println("收到消息：" + message);
    }
}
