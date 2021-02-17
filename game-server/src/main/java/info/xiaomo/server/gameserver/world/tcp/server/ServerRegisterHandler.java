package info.xiaomo.server.gameserver.world.tcp.server;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.server.gameserver.protocol.ServerMessage;
import info.xiaomo.server.gameserver.protocol.Mid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 服务器注册消息返回
 *
 * @author JiangZhiYong
 * @QQ 359135103 2017年6月29日 上午11:15:57
 */
@HandlerEntity(mid = Mid.MID.ServerRegisterRes_VALUE, msg = ServerMessage.ServerRegisterResponse.class, thread = ThreadType.SYNC)
public class ServerRegisterHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterHandler.class);

	@Override
	public void run() {
		ServerMessage.ServerRegisterResponse res = getMsg();
		if (res != null) {
			if (ServerState.MAINTAIN.getState() == res.getServerInfo().getState()) {
				LOGGER.warn("服务器状态变为维护");
			}
		}
		// LOGGER.debug("更新服务器信息返回");
	}

}
