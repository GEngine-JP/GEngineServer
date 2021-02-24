package info.xiaomo.server.rpg.server.game;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.network.*;
import info.xiaomo.gengine.utils.AttributeUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaomo
 */
@Slf4j
public class MessageRouter implements INetworkConsumer {


	private final Map<Integer, IProcessor> processors = new HashMap<>(10);

	private final IMessageAndHandler msgPool;

	public MessageRouter(IMessageAndHandler msgPool) {
		this.msgPool = msgPool;
	}

	public void registerProcessor(int queueId, IProcessor consumer) {
		processors.put(queueId, consumer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void consume(Message msg, Channel channel) {

		//将消息分发到指定的队列中，该队列有可能在同一个进程，也有可能不在同一个进程

		int queueId = 1;

		IProcessor processor = processors.get(queueId);
		if (processor == null) {
			log.error("找不到可用的消息处理器[{}]", queueId);
			return;
		}

		Session session = AttributeUtil.get(channel, SessionKey.SESSION);

		if (session == null) {
			return;
		}

		AbstractHandler handler = msgPool.getHandler(msg.getMsgId());
		handler.setMessage(msg);
		handler.setParam(session);
		log.debug("收到消息:" + msg);

		processor.process(handler);

	}

	public IProcessor getProcessor(int queueId) {
		return processors.get(queueId);
	}

}
