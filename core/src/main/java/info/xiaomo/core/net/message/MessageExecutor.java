package info.xiaomo.core.net.message;

import info.xiaomo.core.net.network.NetworkConsumer;
import info.xiaomo.core.net.network.NetworkEventListener;
import io.netty.channel.ChannelInboundHandlerAdapter;

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
 * Date  : 2017/7/11 15:33
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class MessageExecutor extends ChannelInboundHandlerAdapter {
    private NetworkConsumer consumer;

    private NetworkEventListener listener;

    public MessageExecutor(NetworkConsumer consumer, NetworkEventListener listener) {
        this.consumer = consumer;
        this.listener = listener;
    }
}
