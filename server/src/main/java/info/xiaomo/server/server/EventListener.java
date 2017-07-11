package info.xiaomo.server.server;


import info.xiaomo.core.net.NetworkEventListener;
import io.netty.channel.ChannelHandlerContext;

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
 * Date  : 2017/7/11 16:01
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class EventListener implements NetworkEventListener {
    @Override
    public void onConnected(ChannelHandlerContext ctx) {

    }

    @Override
    public void onDisconnected(ChannelHandlerContext ctx) {

    }

    @Override
    public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {

    }
}
