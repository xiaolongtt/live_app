package org.example.live.id.generate.Enum;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/9/6 16:56
 * @注释
 */
public enum IdTypeEnum {
    USER_ID(1,"用户id");

    int code;
    String desc;

    IdTypeEnum(int i, String desc) {
        this.code = i;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
