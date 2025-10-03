package org.example.live.common.interfaces.Enum;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/3 18:18
 * @注释
 */
public enum GatewayHeaderEnum {
    USER_LOGIN_ID("用户id","qiyu_gh_user_id");

    String desc;
    String name;

    GatewayHeaderEnum(String desc, String name) {
        this.desc = desc;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }
}
