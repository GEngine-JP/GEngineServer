package info.xiaomo.server.server;


import info.xiaomo.gengine.network.mina.config.MinaClientConfig;
import info.xiaomo.gengine.network.mina.service.SingleMinaTcpClientService;

/**
 * 连接集群 tcp客户端
 *
 * Role>
 * 2017年6月28日 下午4:16:19
 */
public class Bydr2ClusterClient extends SingleMinaTcpClientService {

	public Bydr2ClusterClient(MinaClientConfig minaClientConfig) {
		super(minaClientConfig);
	}

	@Override
	protected void running() {
		super.running();
		// ServerThread executor = getExecutor(ThreadType.SYNC);
		// executor.addTimerEvent(new ServerHeartTimer()); //TODO 临时添加
	}
	
	

}
