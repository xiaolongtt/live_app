package org.example.living.constants;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/12 12:27
 * @注释
 */

public enum LivingRoomTypeEnum {
    DEFAULT_LIVING_ROOM(1,"普通直播间"),
    PK_LIVING_ROOM(2,"pk直播间");
    int code;
    String desc;
    LivingRoomTypeEnum(int code, String desc) {
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
