package info.xiaomo.netty.client;

import info.xiaomo.netty.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * mail: hupengbest@163.com
 * QQ_NO: 83387856
 * Date: 2016/11/24 17:31
 * Copyright(©) 2015 by xiaomo.
 **/

public class ClientStarter {

    private Channel connect(int port, String host) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            return future.channel();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {


        Channel channel = new ClientStarter().connect(8080, "127.0.0.1");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String input = scanner.nextLine();
        while (input != null) {
            if (channel != null) {
                channel.writeAndFlush(input);
            }
            input = scanner.nextLine();
        }


    }

}
