package info.xiaomo.server.world.tcp.server;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.protocol.Mid;
import info.xiaomo.server.protocol.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***
 * 返回服务器列表
 * 
 * @author JiangZhiYong
 * @QQ 359135103 2017年6月29日 下午1:58:40
 */
@HandlerEntity(mid = Mid.MID.ServerListRes_VALUE, msg = ServerMessage.ServerListResponse.class)
public class ServerListHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerListHandler.class);

	@Override
	public void run() {
	}

}
