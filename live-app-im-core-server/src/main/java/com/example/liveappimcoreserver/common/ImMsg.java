package com.example.liveappimcoreserver.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/5 15:20
 * @注释 传递的消息类
 */
@Data
public class ImMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = 4826057418842619409L;
    //魔法数字,用于基本校验
    private short magic;
    //body的长度
    private int len;
    //消息类型 ，根据不同的消息类型，去调用不同的handler去处理
    private int code;
    //消息主体
    private byte[] body;
}
