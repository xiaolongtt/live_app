package com.example.liveappiminterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 17:34
 * @注释
 */
public enum AppIdEnum {
    LIVE_BIZ(10001, "旗鱼直播业务");

    private int code;
    private String desc;

    AppIdEnum(int code, String desc) {
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
