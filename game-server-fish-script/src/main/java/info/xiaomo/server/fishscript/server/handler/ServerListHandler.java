package info.xiaomo.server.fishscript.server.handler;

import java.util.*;
import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.server.fish.server.BydrServer;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import info.xiaomo.server.shared.protocol.server.ServerListResponse;

/***
 * 返回服务器列表
 *
 *
 *  2017年6月29日 下午1:58:40
 */
@HandlerEntity(mid = MsgId.ServerListRes_VALUE, msg = ServerListResponse.class)
public class ServerListHandler extends TcpHandler {

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
