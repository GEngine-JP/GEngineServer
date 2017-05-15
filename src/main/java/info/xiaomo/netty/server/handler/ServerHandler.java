package info.xiaomo.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 *
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
     * param ctx ctx
     * param cause cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 接收客户端传来的消息
     * param ctx ctx
     * param msg 消息内容
     * throws Exception Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.writeAndFlush(msg);
    }


    /**
     * 每次创建链接的时候都会调用这个方法
     * param ctx ctx
     * throws Exception Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("创建连接：" + ctx.channel());
    }
}
