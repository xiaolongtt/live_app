package com.example.livebankinterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/11/2 17:52
 * @注释
 */
public enum PayProductTypeEnum {
    QIYU_COIN(0,"直播间充值-旗鱼虚拟币产品");

    PayProductTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    Integer code;
    String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
