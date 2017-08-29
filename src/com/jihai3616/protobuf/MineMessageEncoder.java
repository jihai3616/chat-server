package com.jihai3616.protobuf;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author zoubingbing
 * @date : 2017年8月16日下午5:28:29
 * @Param
 * @return
 */

public class MineMessageEncoder extends MessageToMessageEncoder<Character> {

	@Override
	protected void encode(ChannelHandlerContext arg0, Character arg1, List<Object> arg2) throws Exception {
		arg2.add(arg1);
	}

}
