package com.xcodev.rocketmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.xcodev.rocketmq.config.OrderRocketMqTemplate;
import com.xcodev.rocketmq.config.StockRocketMqTemplate;
import com.xcodev.rocketmq.entity.Order;
import com.xcodev.rocketmq.service.RocketMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 普通消息
 *
 * @author xcodev
 */

@Slf4j
@Service
public class RocketMessageServiceImpl implements RocketMessageService {

    private final RocketMQTemplate rocketMQTemplate;

    private final OrderRocketMqTemplate orderRocketMqTemplate;

    private final StockRocketMqTemplate stockRocketMqTemplate;

    private final String TOPIC = "xcodev_message";

    public RocketMessageServiceImpl(@Qualifier("rocketMQTemplate") RocketMQTemplate rocketMQTemplate,
                                    OrderRocketMqTemplate orderRocketMqTemplate,
                                    StockRocketMqTemplate stockRocketMqTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.orderRocketMqTemplate = orderRocketMqTemplate;
        this.stockRocketMqTemplate = stockRocketMqTemplate;
    }

    @Override
    public void sendMsg(String msg) {
        Message<String> messageBuilder = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.convertAndSend(TOPIC, messageBuilder);
    }

    @Override
    public void asyncSendMessage(String msg) {
        rocketMQTemplate.asyncSend(TOPIC, MessageBuilder.withPayload(msg).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送失败");
            }
        });
    }

    @Override
    public void sendTransactionMsg(String msg) {
        Order order = new Order();
        order.setCode("ORDER_001");
        order.setState(1);
        Message<String> messageBuilder = MessageBuilder
                .withPayload(JSON.toJSONString(order))
                .setHeader(RocketMQHeaders.TRANSACTION_ID, UUID.randomUUID().toString())
                .setHeader(RocketMQHeaders.KEYS, order.getCode())
                .build();

        TransactionSendResult transactionSendResult = orderRocketMqTemplate.sendMessageInTransaction(TOPIC, messageBuilder, null);
        log.info(" 发送状态 {}", transactionSendResult.getLocalTransactionState());
    }

}
