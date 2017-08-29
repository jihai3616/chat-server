package com.jihai3616.netty;

/**
 * @author zoubingbing
 * @date : 2017年8月13日下午6:02:09
 * @Server 聊天服务端程序，服务端实现消息的传递，记录接入客户端名称用于记录客户端的聊天记录，以及服务端具有向客户端广播消息的能力
 * @client 1.首先实现跟服务端的通信  2.然后考虑实现不同客户端之间的通信 
 */

public class test {

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if(args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			}catch(NumberFormatException e) {
				e.printStackTrace();
				// use default port = 8080;
			}
			
		}
		ChatServer server = new ChatServer();
		
		server.bind(port);
		
	}

}
