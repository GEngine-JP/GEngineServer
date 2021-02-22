package info.xiamo.server.robot;

import info.xiamo.server.robot.handle.RobotConsumer;
import info.xiamo.server.robot.handle.RobotEventListener;
import info.xiamo.server.robot.handle.RobotMessagePool;
import info.xiaomo.gengine.network.handler.MessageDecoder;
import info.xiaomo.gengine.network.handler.MessageEncoder;
import info.xiaomo.gengine.network.handler.MessageExecutor;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class);
		b.option(ChannelOption.TCP_NODELAY, true);
		b.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pip = ch.pipeline();
				pip.addLast("NettyMessageDecoder", new MessageDecoder(new RobotMessagePool()));
				pip.addLast("NettyMessageEncoder", new MessageEncoder(new RobotMessagePool()));
				pip.addLast("NettyMessageExecutor",
						new MessageExecutor(new RobotConsumer(), new RobotEventListener()));
			}
		});

		ChannelFuture f = b.connect("127.0.0.1", 9001).sync();

		Channel channel = f.channel();

//		ReqLoginMessage req = new ReqLoginMessage();
//		req.setLoginName("zhangxiaoli");
//		req.setPid(1);
//		req.setSid(1);
//		req.setIDNumber("123456789012345678");
//		req.setClient(1);


//		channel.writeAndFlush(req);


		while (true) {
			Thread.sleep(1000000);
		}

		//f.channel().closeFuture().sync();
		//group.shutdownGracefully();
	}
}
