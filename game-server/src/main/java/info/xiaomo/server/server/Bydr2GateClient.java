package info.xiaomo.server.server;

import info.xiaomo.gengine.network.mina.config.MinaClientConfig;
import info.xiaomo.gengine.network.mina.message.IDMessage;
import info.xiaomo.gengine.network.mina.service.MinaClientGameService;
import info.xiaomo.gengine.network.mina.service.MutilMinaTcpClientGameService;
import info.xiaomo.gengine.network.server.ServerInfo;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.thread.timer.event.ServerHeartTimer;
import info.xiaomo.gengine.utils.SysUtil;
import info.xiaomo.server.thread.RoomExecutor;
import info.xiaomo.server.protocol.ServerMessage;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 捕鱼达人连接大厅 Tcp客户端
 *
 *
 * Role> 2017年6月28日 下午4:12:57
 */
public class Bydr2GateClient extends MutilMinaTcpClientGameService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bydr2GateClient.class);

	public Bydr2GateClient(ThreadPoolExecutorConfig threadPoolExecutorConfig, MinaClientConfig minaClientConfig) {
		super(threadPoolExecutorConfig, minaClientConfig);
	}

	@Override
	protected void running() {
		// 全局同步线程
		ServerThread syncThread = getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());

		// 添加房间线程池
		RoomExecutor roomExecutor = new RoomExecutor();
		getServerThreads().put(ThreadType.ROOM, roomExecutor);
	}

	/**
	 * 消息处理器
	 *
	 *
	 * Role> 2017年7月11日 下午6:29:34
	 */
	public class MutilConHallHandler extends MutilTcpProtocolHandler {

		public MutilConHallHandler(ServerInfo serverInfo, MinaClientGameService service) {
			super(serverInfo, service);
		}

		@Override
		public void sessionOpened(IoSession session) {
			super.sessionOpened(session);
			// 向网关服注册session
			ServerMessage.ServerRegisterRequest.Builder builder = ServerMessage.ServerRegisterRequest.newBuilder();
			ServerMessage.ServerInfo.Builder info = ServerMessage.ServerInfo.newBuilder();
			info.setId(getMinaClientConfig().getId());
			info.setIp("");
			info.setMaxUserCount(1000);
			info.setOnline(1);
			info.setName(getMinaClientConfig().getName());
			info.setState(ServerState.NORMAL.getState());
			info.setType(getMinaClientConfig().getType().getType());
			info.setWwwip("");
			info.setTotalMemory(SysUtil.totalMemory());
			info.setFreeMemory(SysUtil.freeMemory());
//			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class,
//					script -> script.buildServerInfo(info));
			builder.setServerInfo(info);
			session.write(new IDMessage(session, builder.build(), 0));
		}

	}

}
