package info.xiaomo.server;

import info.xiaomo.core.net.message.Message;
import info.xiaomo.core.net.message.MessageProcessor;
import info.xiaomo.core.net.network.NetworkConsumer;
import info.xiaomo.core.util.AttributeUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
 * Date  : 2017/7/11 16:54
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class MessageRouter implements NetworkConsumer {
    private static Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private Map<Integer, MessageProcessor> processors = new HashMap<>();

    public void registerProcessor(int queueId, MessageProcessor consumer) {
        processors.put(queueId, consumer);
    }

    @Override
    public void consume(Channel channel, Message msg) {

        //将消息分发到指定的队列中，该队列有可能在同一个进程，也有可能不在同一个进程

        int queueId = msg.getQueueId();

        MessageProcessor processor = processors.get(queueId);
        if(processor == null){
            LOGGER.error("找不到可用的消息处理器[{}]", queueId);
            return;
        }

        Session session = AttributeUtil.get(channel, SessionKey.SESSION);


        if(session == null){
            return;
        }

        msg.setParam(session);

        LOGGER.debug("收到消息:" + msg);

        processor.process(msg);

    }

    public MessageProcessor getProcessor(int queueId){
        return processors.get(queueId);
    }
}
