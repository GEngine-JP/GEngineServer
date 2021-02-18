package info.xiaomo.server.cluster.manager;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.network.server.ServerType;
import info.xiaomo.server.cluster.server.ClusterTcpServer;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器管理类
 * <p>
 * <p>
 * 2017-04-05
 */
public class ServerManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);

	private static final ServerManager instance = new ServerManager();
	/**
	 * 服务器列表
	 */
	final Map<ServerType, Map<Integer, ServerInfo>> servers = new ConcurrentHashMap<>();

	public static ServerManager getInstance() {
		return instance;
	}

	/**
	 * 注册服务器信息
	 *
	 * @param serverInfo
	 */
	public ServerInfo registerServer(GameServerInfo serverInfo, IoSession session) {
		Map<Integer, ServerInfo> map = servers.get(ServerType.valueOf(serverInfo.getType()));
		if (map == null) {
			map = new ConcurrentHashMap<>();
			servers.put(ServerType.valueOf(serverInfo.getType()), map);
		}
		ServerInfo info = map.get(serverInfo.getId());
		if (info == null) {
			info = new ServerInfo();
			map.put(serverInfo.getId(), info);
		}
		info.setHttpPort(serverInfo.getHttpPort());
		info.setId(serverInfo.getId());
		info.setIp(serverInfo.getIp());
		info.setMaxUserCount(serverInfo.getMaxUserCount());
		info.setName(serverInfo.getName());
		info.setOnline(serverInfo.getOnline());
		info.setPort(serverInfo.getPort());
		info.setState(serverInfo.getState());
		info.setType(serverInfo.getType());
		info.setWwwip(serverInfo.getWwwIp());
		info.onIoSessionConnect(session);
		info.setFreeMemory(serverInfo.getFreeMemory());
		info.setTotalMemory(serverInfo.getTotalMemory());
		info.setVersion(serverInfo.getVersion());

		if (!session.containsAttribute(ClusterTcpServer.SERVER_INFO)) {
			session.setAttribute(ClusterTcpServer.SERVER_INFO, info);
		}
		return info;
	}

	/**
	 * 获取服务器列表
	 *
	 * @param serverType
	 * @return
	 */
	public Map<Integer, ServerInfo> getServers(ServerType serverType) {
		return servers.get(serverType);
	}

	/**
	 * 获取服务器
	 * <p>
	 * <p>
	 * Role> 2017年7月13日 下午3:57:14
	 *
	 * @param serverType
	 * @param serverId
	 * @return
	 */
	public ServerInfo getServer(ServerType serverType, int serverId) {
		Map<Integer, ServerInfo> map = getServers(serverType);
		if (map != null) {
			return map.get(serverId);
		}
		return null;
	}

	public Map<ServerType, Map<Integer, ServerInfo>> getServers() {
		return servers;
	}

	/**
	 * 空闲服务器
	 * <p>
	 * 服务器状态监测,在线人数统计 <br>
	 * 服务健康度检测（除了根据在线人数判断还可根据cpu内存等服务器检测设置优先级）
	 * <p>
	 *
	 * @param version 版本号 null 无版本要求
	 * @return
	 */
	public ServerInfo getIdleGate(String version) {
		Map<Integer, ServerInfo> halls = getServers(ServerType.GATE);
		if (halls == null) {
			return null;
		}
		//版本号检查
		Optional<ServerInfo> findFirst = halls.values().stream()
				.filter(server -> server.getState() == ServerState.NORMAL.ordinal() && server.getSession() != null
						&& server.getSession().isConnected())
				.filter(server -> version == null || version.equals(server.getVersion())).min(Comparator.comparingInt(ServerInfo::getOnline));
		return findFirst.orElse(null);
	}

	/**
	 * 移除服务器
	 *
	 * @param serverInfo
	 */
	public void removeServer(ServerInfo serverInfo) {
		LOGGER.info("服务器关闭：{}", serverInfo.toString());
		ServerType serverType = ServerType.valueOf(serverInfo.getType());
		Map<Integer, ServerInfo> map = servers.get(serverType);
		if (map != null) {
			if (map.remove(serverInfo.getId()) != null) {
				LOGGER.info("服务器{}_{}已移除", serverType.toString(), serverInfo.getId());
			}
		}
	}

}
