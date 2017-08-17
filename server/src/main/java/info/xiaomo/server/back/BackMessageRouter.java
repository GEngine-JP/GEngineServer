package info.xiaomo.server.back;

import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.entity.Session;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackMessageRouter implements NetworkConsumer {
	
	private static Logger LOGGER = LoggerFactory.getLogger(BackMessageRouter.class);

	@Override
	public void consume(Channel channel, AbstractHandler handler) {
		Session session = new Session();
		session.setChannel(channel);
		handler.setParam(session);
		try {
			handler.action();
		} catch (Exception e) {
			LOGGER.error("back server 执行消息发生错误.", e);
		}
	}
	
	
}
