package info.xiaomo.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * QQ_NO: 83387856
 * Date: 2016/11/24 10:16
 * Copyright(©) 2015 by xiaomo.
 **/

public class ServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 出错的时候会调用这个方法
     * param context context
     * param cause cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        context.close();
    }


    /**
     * 接收客户端传来的消息
     * param context context
     * param msg 消息内容
     * throws Exception Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("接收到消息" + byteBuf.toString(CharsetUtil.UTF_8));
        context.write(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //  将未决消息冲刷到 远程节点,并且关 闭该 Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 每次创建链接的时候都会调用这个方法
     * param context context
     * throws Exception Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        System.out.println("创建连接：" + context.channel());
    }
}
