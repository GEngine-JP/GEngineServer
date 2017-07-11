package info.xiaomo.server.server;

import info.xiaomo.core.net.Message;
import info.xiaomo.server.entify.Player;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

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
 * Date  : 2017/7/11 16:59
 * desc  : session
 * Copyright(©) 2017 by xiaomo.
 */
public class Session {

    private Channel channel;

    private Player player;

    public ChannelFuture close() {
        return channel.close();
    }


    public void sendMessage(Message msg) {
        channel.writeAndFlush(msg);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
