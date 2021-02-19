package info.xiaomo.server.gateway.manager;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.network.server.ServerType;
import info.xiaomo.server.gateway.struct.UserSession;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import info.xiaomo.server.shared.protocol.system.SystemErrorCode;
import info.xiaomo.server.shared.protocol.system.SystemErrorResponse;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器管理类
 * 
 *
 *   下午5:49:38
 */
public class ServerManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerManager.class);
	private static volatile ServerManager serverManager;

	/** 服务器列表 */
	private final Map<ServerType, Map<Integer, ServerInfo>> serverMap = new ConcurrentHashMap<>();

	private ServerManager() {

	}

	public static ServerManager getInstance() {
		if (serverManager == null) {
			synchronized (ServerManager.class) {
				if (serverManager == null) {
					serverManager = new ServerManager();
				}
			}
		}
		return serverManager;
	}

	/**
	 * 获取游戏信息
	 * 
	 * @param serverType
	 * @param serverId
	 * @return
	 */
	public ServerInfo getGameServerInfo(ServerType serverType, int serverId) {
		if (serverMap.containsKey(serverType)) {
			return serverMap.get(serverType).get(serverId);
		}
		return null;
	}

	/**
	 * 更新服务器信息
	 * 
	 * @param info
	 */
	public void updateServerInfo(GameServerInfo info) {
		if (info.getType() < 100 && info.getType() != ServerType.HALL.ordinal()) { // 游戏服从101开始定义
																					// 大厅服需要更新
			return;
		}
		ServerType serverType = ServerType.valueOf(info.getType());
		ServerInfo server = getGameServerInfo(serverType, info.getId());
		if (server == null) {
			server = new ServerInfo();
			server.setId(info.getId());
		}
		server.setIp(info.getIp());
		server.setPort(info.getPort());
		server.setOnline(info.getOnline());
		server.setMaxUserCount(info.getMaxUserCount());
		server.setName(info.getName());
		server.setHttpPort(server.getHttpPort());
		server.setState(info.getState());
		server.setType(info.getType());
		server.setWwwip(info.getWwwIp());

		if (!serverMap.containsKey(serverType)) {
			serverMap.put(serverType, new ConcurrentHashMap<>());
		}

		serverMap.get(serverType).put(info.getId(), server);
		// LOGGER.warn("游戏服务器{}注册更新到网关服务器", handler.toString());
	}

	/**
	 * 获取空闲的游戏服务器
	 * 
	 * @param serverType
	 * @return
	 */
	public ServerInfo getIdleGameServer(ServerType serverType, UserSession userSession) {
		Map<Integer, ServerInfo> map = serverMap.get(serverType);
		if (map == null || map.size() == 0) {
			return null;
		}
		Optional<ServerInfo> findFirst = map.values().stream()
				.filter(server -> StringUtil.isNullOrEmpty(userSession.getVersion())
						|| userSession.getVersion().equals(server.getVersion())) // 版本号
				.filter(server -> server.getState() == ServerState.NORMAL.ordinal()) // 状态
				.sorted((s1, s2) -> s1.getOnline() - s2.getOnline()).findFirst();
        return findFirst.orElse(null);
    }

	/**
	 * 构建错误信息
	 * 
	 *
	 *  2017年7月21日 上午10:37:49
	 * @param errorCode
	 * @param msg
	 * @return
	 */
	public SystemErrorResponse buildSystemErrorResponse(SystemErrorCode errorCode, String msg) {
		SystemErrorResponse.Builder builder = SystemErrorResponse.newBuilder();
		builder.setErrorCode(errorCode);
		if (msg != null) {
			builder.setMsg(msg);
		}
		return builder.build();
	}

}
