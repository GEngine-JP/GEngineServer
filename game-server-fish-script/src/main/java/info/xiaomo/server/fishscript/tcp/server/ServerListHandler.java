package info.xiaomo.server.fishscript.tcp.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.server.fish.server.BydrServer;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
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
		ServerListResponse res = getMsg();
		if (res == null) {
			return;
		}
		List<GameServerInfo> list = res.getServerInfoList();
		// 更新服务器信息
		Set<Integer> serverIds = new HashSet<>();
		list.forEach(info -> {
			BydrServer.getInstance().updateGateServerInfo(info);
			serverIds.add(info.getId());
		});
		Map<Integer, ServerInfo> hallServers = BydrServer.getInstance().getBydr2GateClient()
				.getServers();

		if (hallServers.size() != list.size()) {
			List<Integer> ids = new ArrayList<>(hallServers.keySet());
			ids.removeAll(serverIds);
			ids.forEach(serverId -> {
				BydrServer.getInstance().getBydr2GateClient().removeTcpClient(serverId);
			});
		}
	}

}
