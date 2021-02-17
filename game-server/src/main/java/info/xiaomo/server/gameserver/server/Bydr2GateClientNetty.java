package info.xiaomo.server.gameserver.server;


import info.xiaomo.gengine.network.netty.config.NettyClientConfig;
import info.xiaomo.gengine.network.netty.service.MutilNettyTcpClientService;
import info.xiaomo.gengine.thread.ServerThread;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.thread.timer.event.ServerHeartTimer;
import info.xiaomo.server.gameserver.thread.RoomExecutor;

/**
 * netty 连接网关服
 *
 *
 * Role> 2017年9月14日 下午4:52:20
 */
public class Bydr2GateClientNetty extends MutilNettyTcpClientService {

	public Bydr2GateClientNetty(NettyClientConfig nettyClientConfig) {
		super(nettyClientConfig);
	}

	public Bydr2GateClientNetty(ThreadPoolExecutorConfig threadPoolExecutorConfig,
	                            NettyClientConfig nettyClientConfig) {
		super(threadPoolExecutorConfig, nettyClientConfig);
	}

	@Override
	protected void initThread() {
		super.initThread();
		// 全局同步线程
		ServerThread syncThread = getExecutor(ThreadType.SYNC);
		syncThread.addTimerEvent(new ServerHeartTimer());

		// 添加房间线程池
		RoomExecutor roomExecutor = new RoomExecutor();
		getServerThreads().put(ThreadType.ROOM, roomExecutor);
	}

}
