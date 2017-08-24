package info.xiaomo.server.server;

import info.xiaomo.gameCore.base.common.AttributeUtil;
import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.gameCore.protocol.MessageProcessor;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MessageRouter implements NetworkConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private Map<Integer, MessageProcessor> processors = new HashMap<>();

    public void registerProcessor(int queueId, MessageProcessor consumer) {
        processors.put(queueId, consumer);
    }

    @Override
    public void consume(Message msg, Channel channel) {

        //将消息分发到指定的队列中，该队列有可能在同一个进程，也有可能不在同一个进程

        int queueId = msg.getQueueId();

        MessageProcessor processor = processors.get(queueId);
        if (processor == null) {
            LOGGER.error("找不到可用的消息处理器[{}]", queueId);
            return;
        }

        Session session = AttributeUtil.get(channel, SessionKey.SESSION);


        if (session == null) {
            return;
        }

        msg.setParam(session);

        LOGGER.debug("收到消息:" + msg);

        processor.process(msg);

    }

    public MessageProcessor getProcessor(int queueId) {
        return processors.get(queueId);
    }

}
