package com.xcodev.rocketmq;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 消息生产
 *
 * @author xcodev
 */
public class ProducerTest {
    private static final Logger logger = LoggerFactory.getLogger(ProducerTest.class);

    public static void main(String[] args) throws ClientException, IOException, MQBrokerException, RemotingException, InterruptedException, MQClientException {
//        create();


        // 接入点地址，需要设置成Proxy的地址和端口列表，一般是xxx:8080;xxx:8081
        String endpoint = "10.10.0.169:28081;10.10.0.169:28080";
        // 消息发送的目标Topic名称，需要提前创建。
        String topic = "TestTopic";
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder().setEndpoints(endpoint);
//        builder.setNamespace("10.10.0.169:9876;10.10.0.169:9877");
        ClientConfiguration configuration = builder.build();
        // 初始化Producer时需要设置通信配置以及预绑定的Topic。
        Producer producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(configuration)
                .build();
        for (int i = 0; i < 100; i++) {
            // 普通消息发送。
            Message message = provider.newMessageBuilder()
                    .setTopic(topic)
                    // 设置消息索引键，可根据关键字精确查找某条消息。
                    .setKeys("messageKey")
                    // 设置消息Tag，用于消费端根据指定Tag过滤消息。
                    .setTag("messageTag")
                    // 消息体。
                    .setBody(("我就是想测试一下这个东西到底能有啥用" + i).getBytes())
                    .build();
            try {
                // 发送消息，需要关注发送结果，并捕获失败等异常。
                SendReceipt sendReceipt = producer.send(message);
                logger.info("Send message successfully, messageId={}", sendReceipt.getMessageId());
            } catch (ClientException e) {
                logger.error("Failed to send message", e);
            }
        }


        producer.close();
    }

}
