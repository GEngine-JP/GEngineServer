package info.xiaomo.server.robot;

import info.xiaomo.gengine.network.handler.MessageDecoder;
import info.xiaomo.gengine.network.handler.MessageEncoder;
import info.xiaomo.gengine.network.handler.MessageExecutor;
import info.xiaomo.gengine.network.pool.MessageRouter;
import info.xiaomo.server.robot.handle.RobotEventListener;
import info.xiaomo.server.robot.handle.RobotMessagePool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RobotApplication {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class);
        b.option(ChannelOption.TCP_NODELAY, true);
        b.handler(
                new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pip = ch.pipeline();
                        RobotMessagePool pool = new RobotMessagePool();
                        pip.addLast("NettyMessageDecoder", new MessageDecoder(pool));
                        pip.addLast("NettyMessageEncoder", new MessageEncoder(pool));
                        pip.addLast(
                                "NettyMessageExecutor",
                                new MessageExecutor(
                                        new MessageRouter(pool), new RobotEventListener(), pool));
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

        f.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
