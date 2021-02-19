package info.xiaomo.server.hall.server;

import info.xiaomo.gengine.bean.NetPort;
import info.xiaomo.gengine.network.mina.config.MinaClientConfig;
import info.xiaomo.gengine.network.mina.message.IDMessage;
import info.xiaomo.gengine.network.mina.service.MinaClientGameService;
import info.xiaomo.gengine.network.mina.service.MutilMinaTcpClientGameService;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.thread.timer.event.ServerHeartTimer;
import info.xiaomo.server.hall.script.IGameServerCheckScript;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import info.xiaomo.server.shared.protocol.server.ServerRegisterRequest;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 捕鱼达人连接大厅 Tcp客户端
 *
 *
 * 2017年6月28日 下午4:12:57
 */
public class Hall2GateClient extends MutilMinaTcpClientGameService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Hall2GateClient.class);

	public Hall2GateClient(ThreadPoolExecutorConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig) {
		super(threadPoolExecutorConfig, minaClientConfig);
	}

	@Override
	protected void running() {
		ServerThread syncThread = getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());

	}

	/**
	 * 更新大厅服务器信息
	 *
	 * @param info
	 */
	public void updateHallServerInfo(GameServerInfo info) {
		ServerInfo serverInfo = serverMap.get(info.getId());
		if (serverInfo == null) {
			serverInfo = getServerInfo(info);
			addTcpClient(serverInfo, NetPort.GATE_GAME_PORT, new MutilConHallHandler(serverInfo, this));
		} else {
			serverInfo.setIp(info.getIp());
			serverInfo.setId(info.getId());
			serverInfo.setPort(info.getPort());
			serverInfo.setState(info.getState());
			serverInfo.setOnline(info.getOnline());
			serverInfo.setMaxUserCount(info.getMaxUserCount());
			serverInfo.setName(info.getName());
			serverInfo.setHttpPort(info.getHttpPort());
			serverInfo.setWwwip(info.getWwwIp());
		}
		serverMap.put(info.getId(), serverInfo);
	}

	private ServerInfo getServerInfo(GameServerInfo info) {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.setIp(info.getIp());
		serverInfo.setId(info.getId());
		serverInfo.setPort(info.getPort());
		serverInfo.setState(info.getState());
		serverInfo.setOnline(info.getOnline());
		serverInfo.setMaxUserCount(info.getMaxUserCount());
		serverInfo.setName(info.getName());
		serverInfo.setHttpPort(info.getHttpPort());
		serverInfo.setWwwip(info.getWwwIp());
		return serverInfo;
	}

	/**
	 * 消息处理器
	 *
	 *
	 * 2017年7月11日 下午6:29:34
	 */
	public class MutilConHallHandler extends MutilTcpProtocolHandler {

		public MutilConHallHandler(ServerInfo serverInfo, MinaClientGameService service) {
			super(serverInfo, service);
		}

		@Override
		public void sessionOpened(IoSession session) {
			super.sessionOpened(session);
			ServerRegisterRequest.Builder builder = ServerRegisterRequest.newBuilder();
			GameServerInfo.Builder info = GameServerInfo.newBuilder();
			info.setId(getMinaClientConfig().getId());
			info.setIp("");
			info.setMaxUserCount(1000);
			info.setOnline(1);
			info.setName(getMinaClientConfig().getName());
			info.setState(ServerState.NORMAL.getState());
			info.setType(getMinaClientConfig().getType().getType());
			info.setWwwIp("");
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class,
					script -> script.buildServerInfo(info));
			builder.setServerInfo(info);
			session.write(new IDMessage(session, builder.build(), 0));
		}

	}

}
