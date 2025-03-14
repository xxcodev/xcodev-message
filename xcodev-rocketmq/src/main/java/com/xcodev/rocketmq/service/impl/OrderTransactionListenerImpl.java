package com.xcodev.rocketmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.xcodev.rocketmq.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;

import java.util.HashMap;

/**
 * @author xcodev
 */

@Slf4j
@RocketMQTransactionListener(rocketMQTemplateBeanName = "orderRocketMqTemplate")
public class OrderTransactionListenerImpl implements RocketMQLocalTransactionListener {

    private final HashMap<String, Order> orderMap = new HashMap<>(16);

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        try {
            log.info("事物消息Header为{}", msg.getHeaders());
            String payload = new String((byte[]) msg.getPayload());
            log.info("事物消息Payload为{}", payload);
            Order order = JSON.parseObject(payload, Order.class);
            Order oldOrder = orderMap.get(order.getCode());
            if (oldOrder == null || oldOrder.getState() == 1) {
                order.setState(2);
                orderMap.put(order.getCode(), order);
                return RocketMQLocalTransactionState.UNKNOWN;
            }
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String orderCode = (String) msg.getHeaders().get("rocketmq_KEYS");
        Order order = orderMap.get(orderCode);
        if (order.getState().equals(2)) {
            return RocketMQLocalTransactionState.COMMIT;
        }

        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
