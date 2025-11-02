package com.example.livebankinterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 23:00
 * @注释 支付订单状态枚举
 */
public enum OrderStatusEnum {
    WAITING_PAY(0,"待支付"),
    PAYING(1,"支付中"),
    PAYED(2,"已支付"),
    PAY_BACK(3,"撤销"),
    IN_VALID(4,"无效");

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
