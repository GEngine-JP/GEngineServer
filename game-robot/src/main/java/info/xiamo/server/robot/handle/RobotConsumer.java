package info.xiamo.server.robot.handle;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.SessionKey;
import info.xiaomo.gengine.utils.AttributeUtil;
import info.xiaomo.server.rpg.server.Session;
import io.netty.channel.Channel;

public class RobotConsumer implements INetworkConsumer {

	@Override
	public void consume(AbstractMessage msg, Channel channel) {
		Session session = (Session) AttributeUtil.get(channel, SessionKey.SESSION);

		if (session == null) {
			return;
		}

//		msg.setParam(session);
//		msg.doAction();

	}

}
