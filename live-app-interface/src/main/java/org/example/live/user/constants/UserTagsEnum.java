package org.example.live.user.constants;


/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/6 20:09
 * @注释
 */

public enum UserTagsEnum {
    IS_BLACK((long) Math.pow(2,0),"是否为封禁账号","tag_info_01"),
    IS_VIP((long) Math.pow(2,1),"是否为会员","tag_info_01"),
    IS_OLD_USER((long) Math.pow(2,2),"是否为老用户","tag_info_01"),
    ;
    long tag;
    String desc;
    String fieldName;

    UserTagsEnum(long tag, String desc, String fieldName) {
        this.tag = tag;
        this.desc = desc;
        this.fieldName = fieldName;
    }

    public long getTag() {
        return tag;
    }

    public String getDesc() {
        return desc;
    }

    public String getFieldName() {
        return fieldName;
    }

}
