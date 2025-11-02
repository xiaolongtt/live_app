package com.example.livebankinterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 16:27
 * @注释 支付渠道枚举类
 */
public enum PayChannelEnum {
    ZHI_FU_BAO(0, "支付宝"),
    WEI_XIN(1, "微信"),
    YIN_LIAN(2, "银联"),
    SHOU_YIN_TAI(3, "收银台");
    private int code;
    private String desc;

    PayChannelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaySourceEnum find(int code) {
        for (PaySourceEnum value : PaySourceEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
