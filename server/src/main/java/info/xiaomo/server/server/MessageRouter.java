package info.xiaomo.server.server;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.core.base.common.AttributeUtil;
import info.xiaomo.core.network.AbstractHandler;
import info.xiaomo.core.network.IMessageAndHandler;
import info.xiaomo.core.network.IProcessor;
import info.xiaomo.core.network.INetworkConsumer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomo
 */
public class MessageRouter implements INetworkConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private Map<Integer, IProcessor> processors = new HashMap<>(10);

    private IMessageAndHandler msgPool;

    public MessageRouter(IMessageAndHandler msgPool) {
        this.msgPool = msgPool;
    }

    public void registerProcessor(int queueId, IProcessor consumer) {
        processors.put(queueId, consumer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void consume(AbstractMessage msg, Channel channel) {

        //将消息分发到指定的队列中，该队列有可能在同一个进程，也有可能不在同一个进程

        int queueId = 1;

        IProcessor processor = processors.get(queueId);
        if (processor == null) {
            LOGGER.error("找不到可用的消息处理器[{}]", queueId);
            return;
        }

        Session session = AttributeUtil.get(channel, SessionKey.SESSION);

        if (session == null) {
            return;
        }

        AbstractHandler handler = msgPool.getHandler(msg.getClass().getName());
        handler.setMessage(msg);
        handler.setParam(session);
        LOGGER.debug("收到消息:" + msg);

        processor.process(handler);

    }

    public IProcessor getProcessor(int queueId) {
        return processors.get(queueId);
    }

}
