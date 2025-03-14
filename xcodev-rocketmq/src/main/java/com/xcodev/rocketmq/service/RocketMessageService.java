package com.xcodev.rocketmq.service;

/**
 * @author xcodev
 */
public interface RocketMessageService {

    /**
     * 发送普通消息
     *
     * @param msg 消息
     */
    void sendMsg(String msg);

    /**
     * 异步发送
     *
     * @param msg 消息
     */
    void asyncSendMessage(String msg);

    /**
     * 发送事物消息
     */
    void sendTransactionMsg(String msg);
}
