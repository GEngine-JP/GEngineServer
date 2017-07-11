package info.xiaomo.core.net.message;


import info.xiaomo.core.queue.IQueueDriverCommand;

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
 * Date  : 2017/7/11 15:41
 * desc  : 消息接口
 * Copyright(©) 2017 by xiaomo.
 */
public interface Message extends IQueueDriverCommand {

    void decode(byte[] bytes);

    byte[] encode();

    int length();

    void setLength(int length);

    int getId();

    int getQueueId();

    void setSequence(short sequence);

    short getSequence();
}
