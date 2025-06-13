package org.example.live.msg.provicer.Service;

import org.example.live.msg.constants.MsgSendResultEnum;
import org.example.live.msg.dto.MsgCheckDTO;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 18:09
 * @注释
 */
public interface IMsgRpcService {
    /**
     * 发送短信接口
     *
     * @param phone
     * @return
     */
    MsgSendResultEnum sendMessage(String phone);

    /**
     * 校验登录验证码
     *
     * @param phone
     * @param code
     * @return
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);

    /**
     * 插入一条短信记录
     *
     * @param phone
     * @param code
     */
    void insertOne(String phone, Integer code);
}
