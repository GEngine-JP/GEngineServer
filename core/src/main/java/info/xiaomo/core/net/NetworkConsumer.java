package info.xiaomo.core.net;

import io.netty.channel.Channel;

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
 * Date  : 2017/7/11 15:36
 * desc  : 消费者接口
 * Copyright(©) 2017 by xiaomo.
 */
public interface NetworkConsumer {
	void consume(Channel channel, Message msg);
}
