package info.xiaomo.server.gateway.server;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.network.mina.config.MinaClientConfig;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.utils.FileUtil;
import info.xiaomo.gengine.utils.SysUtil;
import info.xiaomo.server.gateway.AppGateWay;
import info.xiaomo.server.gateway.manager.UserSessionManager;
import info.xiaomo.server.gateway.server.client.Gate2ClusterClient;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import info.xiaomo.server.shared.protocol.server.ServerRegisterRequest;
import info.xiaomo.server.shared.rediskey.GateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关服务器
 *
 * 2017-03-30
 */
public class GateServer implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(GateServer.class);

	private final GateTcpUserServer gateTcpUserServer; // 用户tcp
	private final Gate2ClusterClient clusterClient; // 链接集群服
	private final GateTcpGameServer gateTcpGameServer; // 内部转发tcp
	private GateUdpUserServer gateUdpUserServer; // 用户udp;
	private GateWebSocketUserServer gateWebSocketUserServer; // 用户websocket
	private final GateHttpServer gateHttpServer; // HTTP通信

	public GateServer() {
		// 用户Tcp服务器
		ThreadPoolExecutorConfig threadPoolExecutorConfig =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(),
						"threadPoolExecutorConfig.yml",
						ThreadPoolExecutorConfig.class);
		if (threadPoolExecutorConfig == null) {
			LOGGER.error("线程池配置不存在");
			System.exit(1);
		}
		MinaServerConfig minaServerConfig_user =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(), "minaServerConfig_user.yml", MinaServerConfig.class);
		if (minaServerConfig_user == null) {
			LOGGER.error("mina服务器配置不存在");
			System.exit(1);
		}
		MinaServerConfig minaServerConfig_udp_user =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(), "minaServerConfig_udp_user.yml", MinaServerConfig.class);

		MinaServerConfig minaServerConfig_websocket_user =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(), "minaServerConfig_websocket_user.yml", MinaServerConfig.class);

		// HTTP通信
		MinaServerConfig minaServerConfig_http =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(), "minaServerConfig_http.yml", MinaServerConfig.class);

		gateTcpUserServer = new GateTcpUserServer(threadPoolExecutorConfig, minaServerConfig_user);
		// 开启udp服务
		if (minaServerConfig_udp_user != null) {
			gateUdpUserServer = new GateUdpUserServer(minaServerConfig_udp_user);
		}

		// 开启webSocket
		if (minaServerConfig_websocket_user != null) {
			gateWebSocketUserServer = new GateWebSocketUserServer(minaServerConfig_websocket_user);
		}

		// http服务
		gateHttpServer = new GateHttpServer(minaServerConfig_http);

		// 游戏tcp 服务器
		MinaServerConfig minaServerConfig_game =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(), "minaServerConfig_game.yml", MinaServerConfig.class);
		if (minaServerConfig_game == null) {
			LOGGER.error("mina服务器配置不存在");
			System.exit(1);
		}

		gateTcpGameServer = new GateTcpGameServer(minaServerConfig_game);

		// 连接Cluster集群客户端
		MinaClientConfig minaClientConfig_cluster =
				FileUtil.getConfigXML(
						AppGateWay.getConfigPath(), "minaClientConfig_cluster.yml", MinaClientConfig.class);
		if (minaClientConfig_cluster == null) {
			LOGGER.error("mina连接集群配置不存在");
			System.exit(1);
		}
		clusterClient = new Gate2ClusterClient(minaClientConfig_cluster);

		Config.SERVER_ID = minaServerConfig_user.getId();

		// 服务器启动时间 测试redis
		String starTimeKey = GateKey.GM_Gate_StartTime.getKey(minaServerConfig_game.getId());
		if (JedisManager.getJedisCluster().exists(starTimeKey)) {
			JedisManager.getJedisCluster().set(starTimeKey, "" + System.currentTimeMillis());
		}
	}

	public static GateServer getInstance() {
		return AppGateWay.getHallServer();
	}

	@Override
	public void run() {
		new Thread(gateTcpUserServer).start();
		new Thread(clusterClient).start();
		new Thread(gateTcpGameServer).start();
		new Thread(gateHttpServer).start();
		if (gateUdpUserServer != null) {
			new Thread(gateUdpUserServer).start();
		}
		if (gateWebSocketUserServer != null) {
			new Thread(gateWebSocketUserServer).start();
		}
	}

	/**
	 * 构建服务器更新注册信息
	 */
	public ServerRegisterRequest buildServerRegisterRequest(
			MinaServerConfig minaServerConfig) {
		ServerRegisterRequest.Builder builder =
				ServerRegisterRequest.newBuilder();
		GameServerInfo.Builder info = GameServerInfo.newBuilder();
		info.setId(minaServerConfig.getId());
		info.setIp(minaServerConfig.getIp());
		info.setMaxUserCount(1000);
		info.setOnline(UserSessionManager.getInstance().getOlineCount());
		info.setName(minaServerConfig.getName());
		info.setState(ServerState.NORMAL.getState());
		info.setType(minaServerConfig.getType());
		info.setWwwIp("");
		info.setPort(minaServerConfig.getPort());
		info.setHttpPort(minaServerConfig.getHttpPort());
		info.setFreeMemory(SysUtil.freeMemory());
		info.setVersion(minaServerConfig.getVersion());
		info.setTotalMemory(SysUtil.totalMemory());
		builder.setServerInfo(info);
		return builder.build();
	}

	public GateTcpUserServer getGateTcpUserServer() {
		return gateTcpUserServer;
	}

	public Gate2ClusterClient getClusterClient() {
		return clusterClient;
	}
}
