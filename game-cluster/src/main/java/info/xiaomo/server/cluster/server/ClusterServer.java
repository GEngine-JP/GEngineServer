package info.xiaomo.server.cluster.server;

import info.xiaomo.gengine.common.bean.NetPort;
import info.xiaomo.gengine.common.utils.SysUtil;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.server.cluster.AppCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 集群管理服务器
 *
 *
 * @date 2017-03-31
 */
public class ClusterServer implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(ClusterServer.class);

	private final ClusterHttpServer clusterHttpServer;
	private final ClusterTcpServer clusterTcpServer;

	public ClusterServer(ThreadPoolExecutorConfig defaultThreadExecutorConfig4HttpServer,
	                     MinaServerConfig minaServerConfig4HttpServer, ThreadPoolExecutorConfig defaultThreadExecutorConfig4TcpServer,
	                     MinaServerConfig minaServerConfig4TcpServer) {

		NetPort.CLUSTER_PORT = minaServerConfig4TcpServer.getPort();
		NetPort.CLUSTER_HTTP_PORT = minaServerConfig4HttpServer.getHttpPort();
		clusterHttpServer = new ClusterHttpServer(defaultThreadExecutorConfig4HttpServer, minaServerConfig4HttpServer);
		clusterTcpServer = new ClusterTcpServer(defaultThreadExecutorConfig4TcpServer, minaServerConfig4TcpServer);
	}

	public static ClusterServer getInstance() {
		return AppCluster.getClusterServer();
	}

	@Override
	public void run() {
		// mainServerThread.addTimerEvent(new ServerHeartTimer());

		// 启动mina相关
		log.info("ClusterServer::clusterHttpServer::start!!!");
		new Thread(clusterHttpServer).start();
		log.info("ClusterServer::clusterTcpServer::start!!!");
		new Thread(clusterTcpServer).start();

		ScriptManager.getInstance().init((str) -> SysUtil.exit(this.getClass(), null, "加载脚本错误"));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			log.error("", ex);
		}
		log.info("---->集群服启动成功<----");
	}

	public ClusterHttpServer getLoginHttpServer() {
		return clusterHttpServer;
	}

	public ClusterTcpServer getLoginTcpServer() {
		return clusterTcpServer;
	}

}
