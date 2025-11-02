package com.example.livebankinterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/1 16:25
 * @注释 支付来源枚举类
 */
public enum PaySourceEnum {
    QIYU_LIVING_ROOM(1, "旗鱼直播间内支付"),
    QIYU_USER_CENTER(2, "用户中心内支付");

    private int code;
    private String desc;

    PaySourceEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    //根据code获取枚举值
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
