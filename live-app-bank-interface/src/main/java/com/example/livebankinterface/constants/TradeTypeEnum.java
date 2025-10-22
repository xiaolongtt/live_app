package com.example.livebankinterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/20 20:14
 * @注释
 */
public enum TradeTypeEnum {
    SEND_GIFT_TRADE(0,"送礼物交易"),
    LIVING_RECHARGE(1,"直播间充值");

    int code;
    String desc;

    TradeTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
