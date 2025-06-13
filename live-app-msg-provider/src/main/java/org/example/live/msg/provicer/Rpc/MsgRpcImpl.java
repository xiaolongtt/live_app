package org.example.live.msg.provicer.Rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.live.msg.constants.MsgSendResultEnum;
import org.example.live.msg.dto.MsgCheckDTO;
import org.example.live.msg.interfaces.IMsgRpc;
import org.example.live.msg.provicer.Service.IMsgRpcService;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 18:08
 * @注释
 */
@DubboService
public class MsgRpcImpl implements IMsgRpc {
    @Resource
    private IMsgRpcService msgRpcService;
    @Override
    public MsgSendResultEnum sendMessage(String phone) {
        return msgRpcService.sendMessage(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return msgRpcService.checkLoginCode(phone,code);
    }

    @Override
    public void insertOne(String phone, Integer code) {
        msgRpcService.insertOne(phone,code);
    }
}
