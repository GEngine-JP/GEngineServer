package info.xiaomo.server.fish.world.tcp.server;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.server.ServerListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * 返回服务器列表
 * 
 *
 *  2017年6月29日 下午1:58:40
 */
@HandlerEntity(mid = MsgId.ServerListRes_VALUE, msg = ServerListResponse.class)
public class ServerListHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerListHandler.class);

	@Override
	public void run() {
	}

}
