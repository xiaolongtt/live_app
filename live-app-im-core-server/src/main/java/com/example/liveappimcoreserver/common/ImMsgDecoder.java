package com.example.liveappimcoreserver.common;

import com.example.liveappiminterface.constants.ImConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/10/5 15:23
 * @注释 消息解码器
 */
public class ImMsgDecoder extends ByteToMessageDecoder {

    private final Integer BASE_LEN=2+4+4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes()>=BASE_LEN){
            //先使用魔法数进行基本判定
            if(byteBuf.readShort()!=ImConstants.DEFAULT_MAGIC_NUMBER){
                channelHandlerContext.close();
                return;
            }
            //读取消息主体的长度
            int len=byteBuf.readInt();
            //读取消息类型
            int code=byteBuf.readInt();
            if(byteBuf.readableBytes()<len){
                channelHandlerContext.close();
                return;
            }
            byte[] body=new byte[len];
            byteBuf.readBytes(body);
            ImMsg imMsg=new ImMsg();
            imMsg.setMagic(ImConstants.DEFAULT_MAGIC_NUMBER);
            imMsg.setLen(len);
            imMsg.setCode(code);
            imMsg.setBody(body);
            //将转换出来的消息发送给下游处理
            list.add(imMsg);
        }
        channelHandlerContext.close();
        return;
    }
}
