package com.jihai3616.protobuf;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author zoubingbing
 * @date : 2017年8月16日下午5:27:38
 * @Param
 * @return
 */

public class MineMessageDecoder extends ByteToMessageDecoder {
	private final int fixedLength;
	
	public MineMessageDecoder(int fixedLength) {
		if(fixedLength < 0) {
			throw new IllegalArgumentException("fixedLength must be a positive integer: " + fixedLength);
		}
		
		this.fixedLength = fixedLength;
	}



	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.readableBytes() >= fixedLength) {
			ByteBuf buf = in.readBytes(fixedLength);
			out.add(buf);
		}
//		if (in.readableBytes() >= 2) {
//            out.add(in.readChar());
//        }
	}

}
