package info.xiaomo.server.rpg.server.back;


import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.gengine.network.IMessageAndHandler;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.Message;
import io.netty.channel.Channel;

/**
 * @author qq
 */
public class BackMessageRouter implements INetworkConsumer {

	private final IMessageAndHandler msgPool;

	public BackMessageRouter(IMessageAndHandler msgPool) {
		this.msgPool = msgPool;
	}

	@Override
	public void consume(Message message, Channel channel) {
		AbstractHandler handler = msgPool.getHandler(message.getMsgId());
		handler.doAction();
	}

}
