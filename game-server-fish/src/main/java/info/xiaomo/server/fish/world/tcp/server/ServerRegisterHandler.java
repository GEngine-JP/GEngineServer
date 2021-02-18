package info.xiaomo.server.fish.world.tcp.server;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.server.ServerRegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 服务器注册消息返回
 *
 *
 *  2017年6月29日 上午11:15:57
 */
@HandlerEntity(mid = MsgId.ServerRegisterRes_VALUE, msg = ServerRegisterResponse.class, thread = ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterHandler.class);

	@Override
	public void run() {
		ServerRegisterResponse res = getMsg();
		if (res != null) {
			if (ServerState.MAINTAIN.getState() == res.getServerInfo().getState()) {
				LOGGER.warn("服务器状态变为维护");
			}
		}
		// LOGGER.debug("更新服务器信息返回");
	}

}
