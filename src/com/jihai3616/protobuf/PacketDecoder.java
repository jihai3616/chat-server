package com.jihai3616.protobuf;

import java.util.List;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by Administrator on 2016/1/29.
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        in.markReaderIndex();

        if (in.readableBytes() < 4) {
            System.out.println("readableBytes length less than 4 bytes, ignored");
            in.resetReaderIndex();
            return;
        }

        int length = in.readInt();

        if (length < 0) {
            ctx.close();
            System.err.println("message length less than 0, channel closed");
            return;
        }

        if (length > in.readableBytes() - 4) {
            //注意！编解码器加这种in.readInt()日志，在大并发的情况下很可能会抛数组越界异常！
            //logger.error("message received is incomplete,ptoNum:{}, length:{}, readable:{}", in.readInt(), length, in.readableBytes());
            in.resetReaderIndex();
            return;
        }

        int ptoNum = in.readInt();


        ByteBuf byteBuf = Unpooled.buffer(length);

        in.readBytes(byteBuf);

        try {
            /* 解密消息体
            ThreeDES des = ctx.channel().attr(ClientAttr.ENCRYPT).get();
            byte[] bareByte = des.decrypt(inByte);*/

            byte[] body= byteBuf.array();

            Message msg = ParseMap.getMessage(ptoNum, body);
            out.add(msg);
            System.out.printf("GateServer Received Message: content length {}, ptoNum: {}", length, ptoNum);

        } catch (Exception e) {
            System.err.printf(ctx.channel().remoteAddress() + ",decode failed.", e);
        }
    }

}
