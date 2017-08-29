package com.jihai3616.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author zoubingbing
 * @date : 2017年8月14日下午2:03:27
 * @Param
 * @return
 */

@Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	// If you need to broadcast a message to more than one Channel, you can use DefaultChannelGroup
	private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
		// broadcast Message
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
            if (channel != incoming){
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]: " + str + "\r\n");
            } else {
            	channel.writeAndFlush("[you]: " + str + "\r\n");
            }
        }
		
		// server record the chat message
		System.out.println("[" + incoming.remoteAddress() + "]: " + str);
	}
	
	@Override	// 当有新的Channel连接到server被调用，即调用了bind/connect并已经就绪
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
        
        // Broadcast message to multiple Channels
        channels.writeAndFlush("[ChatServer] - " + incoming.remoteAddress() + " is online\r\n");
        
        channels.add(ctx.channel());
        
		System.out.println("client[" + incoming.remoteAddress() + "] is online!!!");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
        
        // Broadcast message to multiple Channels
        channels.writeAndFlush("[ChatServer] - " + incoming.remoteAddress() + " is offline\r\n");
        
		System.out.println("client(" + incoming.remoteAddress() + ") is offline!!!");
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		System.out.println("ChatServer has occured exception!!!");
		System.out.println("client[" + ctx.channel().remoteAddress() + "] has occured exception!!!");
		
		cause.printStackTrace();
		ctx.close();
	}

}

