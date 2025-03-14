package com.xcodev.rocketmq;

import com.xcodev.rocketmq.service.RocketMessageService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xcodev
 */
@SpringBootTest
public class XcodevRocketMQTest {
    @Resource
    private  RocketMessageService rocketMessageService;


    @Test
    public void sendMsg() throws InterruptedException {
        rocketMessageService.sendTransactionMsg("这事测试我的第一个订单");

        Thread.sleep(1_000_000_000);
    }
}
