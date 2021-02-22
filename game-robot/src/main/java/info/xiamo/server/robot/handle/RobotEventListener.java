package info.xiamo.server.robot.handle;

import info.xiaomo.gengine.network.INetworkEventListener;
import info.xiaomo.gengine.network.SessionKey;
import info.xiaomo.gengine.utils.AttributeUtil;
import info.xiaomo.server.rpg.server.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobotEventListener implements INetworkEventListener {

	static Logger LOGGER = LoggerFactory.getLogger(RobotEventListener.class);

	@Override
	public void onConnected(ChannelHandlerContext ctx) {
		Channel channel = ctx.channel();
		Session session = (Session) AttributeUtil.get(channel, SessionKey.SESSION);
		if (session == null) {
			session = new Session(channel);
			AttributeUtil.set(channel, SessionKey.SESSION, session);
			LOGGER.error("接收到新的连接：" + channel.toString());
		} else {
			LOGGER.error("新连接建立时已存在Session，注意排查原因" + channel.toString());
		}
	}

	@Override
	public void onDisconnected(ChannelHandlerContext ctx) {

	}

	@Override
	public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {

	}

}
