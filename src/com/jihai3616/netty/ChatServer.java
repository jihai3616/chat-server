package com.jihai3616.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author zoubingbing
 * @date : 2017年8月13日下午6:47:07
 * @Param
 * @return
 */

public class ChatServer {

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();	// client连接处理group
		EventLoopGroup workGroup = new NioEventLoopGroup(); // 时间处理group
		
		try {
			ServerBootstrap sb = new ServerBootstrap();	// 服务器启动的辅助类
			sb.group(bossGroup, workGroup) // 绑定group
				.channel(NioServerSocketChannel.class)	// 指定所使用NIO传输Channel的实现类
				.option(ChannelOption.SO_BACKLOG, 1024) // 保持连接数
				.childOption(ChannelOption.SO_KEEPALIVE, true) // 保持连接
				.localAddress(port)	// 绑定到本地端口，也可是在后面sb.bind(port)进行绑定
				// 当一个新连接被接受时，一个新的子channel将会被创建，ChannelInitializer会将...Handler的实例添加到PipeLine
				.childHandler(new ChannelInitializer<SocketChannel>() {
					
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						ChannelPipeline cp = sc.pipeline();
						
						// 为了解决拆包和粘包，增加LineBasedFrameDecoder和StringDecoder
						cp.addLast("frame", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
						cp.addLast("decoder", new StringDecoder());
						cp.addLast("encoder", new StringEncoder());
						cp.addLast("handler", new ChatServerHandler());
						
//						System.out.println("ChatServer: client(" + sc.remoteAddress() + ") has connected!!!");
					}
			});
			// 异步地绑定服务器，调用sync()阻塞等待直到绑定完成
			ChannelFuture cf = sb.bind().sync();
			
			System.out.println("ChatServer is started, waiting client connection!!!");
			// 获取channel的closeFuture，然后调用sync()阻塞当前线程直到它完成
			cf.channel().closeFuture().sync();
		} finally {
			System.out.println("ChatServer server is going to close!!!");
			// 关闭EventLoopGroup，释放所有的资源
			bossGroup.shutdownGracefully().sync();
			workGroup.shutdownGracefully().sync();
			
			System.out.println("ChatServer server is closed！！！");
		}
		
	}
	
}
