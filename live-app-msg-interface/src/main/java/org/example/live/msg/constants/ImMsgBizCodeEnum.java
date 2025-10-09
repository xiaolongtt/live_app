package org.example.live.msg.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/9 19:38
 * @注释
 */
public enum ImMsgBizCodeEnum {

    CHAT(1001, "聊天业务线"),

    LIVESTREAM(1002, "直播业务线");
    int code;
    String desc;

    ImMsgBizCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
