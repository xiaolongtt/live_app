package com.example.liveappiminterface.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/6 10:31
 * @注释 IM消息码枚举类
 */
public enum ImCodeEnum {
    IM_LOGIN(1001,"登录消息包"),
    IM_LOGOUT(1002,"登出消息包"),
    IM_MSG_BODY(1003,"业务消息包"),
    IM_HEARTBEAT(1004,"心跳消息包");
    private int code;
    private String dsc;

    ImCodeEnum(int i, String dsc) {
        this.code = i;
        this.dsc = dsc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    @Override
    public String toString() {
        return "ImCodeEnum{" +
                "code=" + code +
                ", dsc='" + dsc + '\'' +
                '}';
    }
}
