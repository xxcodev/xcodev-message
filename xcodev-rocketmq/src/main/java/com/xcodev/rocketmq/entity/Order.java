package com.xcodev.rocketmq.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xcodev
 */

@Data
public class Order implements Serializable {

    /**
     * 订单编号
     */
    private String code;

    /**
     * 订单状态 1-待支付 2-已支付 3-已取消
     */
    private Integer state;
}
