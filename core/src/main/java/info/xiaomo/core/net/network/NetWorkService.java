package info.xiaomo.core.net.network;

import info.xiaomo.core.net.message.MessageDecoder;
import info.xiaomo.core.net.message.MessageEncoder;
import info.xiaomo.core.net.message.MessageExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 15:13
 * desc  :
 * 网络服务,负责以下几个事情:</br>
 * 1.网络服务提供TCP协议的接受和发送</br>
 * 2.通信协议的编码和解码</br>
 * 3.调用消费则消费消息</br>
 * Copyright(©) 2017 by xiaomo.
 */
public class NetWorkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetWorkService.class);

    private int port;

    private ServerBootstrap bootstrap;

    private int state;

    private static final byte STATE_STOP = 0;
    private static final byte STATE_START = 1;

    public NetWorkService(NetWorkServiceBuilder builder) {
        int bossLoopGroupCount = builder.getBossLoopGroupCount();
        int workerLoopGroupCount = builder.getWorkerLoopGroupCount();
        this.port = builder.getPort();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(bossLoopGroupCount);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(workerLoopGroupCount);

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childOption(ChannelOption.SO_RCVBUF, 128 * 1024);
        bootstrap.childOption(ChannelOption.SO_SNDBUF, 128 * 1024);
        bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pip = ch.pipeline();
                pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMsgPool()));
                pip.addLast("NettyMessageEncoder", new MessageEncoder());
                MessageExecutor executor = new MessageExecutor(builder.getConsumer(), builder.getNetworkEventListener());
                pip.addLast("NettyMessageExecutor", executor);
                for (ChannelHandler handler : builder.getChannelHandlerList()) {
                    pip.addLast(handler);
                }
            }
        });
    }

    public void start(){
        try {
            ChannelFuture future = bootstrap.bind(port);
            future.sync();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        this.state = STATE_START;
        LOGGER.info("Server on port:{} is start", port);

    }

    public boolean isRunning() {
        return this.state == STATE_START;
    }
}
