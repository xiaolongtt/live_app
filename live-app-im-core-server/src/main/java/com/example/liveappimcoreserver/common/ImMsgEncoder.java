package com.example.liveappimcoreserver.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/5 15:23
 * @注释 消息编码器
 */
public class ImMsgEncoder extends MessageToByteEncoder<ImMsg> {
    //Metty中的编码器
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ImMsg imMsg, ByteBuf byteBuf) throws Exception {
        //写入魔法数字
        byteBuf.writeShort(imMsg.getMagic());
        //写入body的长度
        byteBuf.writeInt(imMsg.getLen());
        //写入消息类型
        byteBuf.writeInt(imMsg.getCode());
        //写入消息主体
        byteBuf.writeBytes(imMsg.getBody());
    }
}
