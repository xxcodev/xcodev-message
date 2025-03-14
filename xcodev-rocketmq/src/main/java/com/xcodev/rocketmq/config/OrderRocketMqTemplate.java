package com.xcodev.rocketmq.config;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * @author xcodev
 */

@ExtRocketMQTemplateConfiguration(group = "order_transaction")
public class OrderRocketMqTemplate extends RocketMQTemplate {
}
