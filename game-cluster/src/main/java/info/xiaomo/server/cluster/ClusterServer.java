package info.xiaomo.server.cluster;

import info.xiaomo.gengine.bean.NetPort;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.persist.redis.jedis.JedisClusterConfig;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.utils.PathUtil;
import info.xiaomo.gengine.utils.SysUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.cluster.server.ClusterHttpServer;
import info.xiaomo.server.cluster.server.ClusterTcpServer;
import info.xiaomo.server.shared.config.Configs;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册中心服务器
 * 2017-03-31
 */
@Data
public class ClusterServer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ClusterServer.class);

    private static ClusterHttpServer clusterHttpServer;
    private static ClusterTcpServer clusterTcpServer;
    private static String path = PathUtil.getConfigPath();

    private ClusterServer() {
    }

    public static ClusterServer getInstance() {
        log.info("配置路径为：" + path);
        ClusterServer clusterServer = new ClusterServer();

        // http server
        MinaServerConfig minaServerConfig_tcp =
                YamlUtil.read(path + Configs.minaServerConfig_tcp, MinaServerConfig.class);
        if (minaServerConfig_tcp == null) {
            SysUtil.exit(AppCluster.class, null, Configs.minaServerConfig_tcp);
            return null;
        }
        MinaServerConfig minaServerConfig_http =
                YamlUtil.read(path + Configs.minaServerConfig_http, MinaServerConfig.class);
        if (minaServerConfig_http == null) {
            SysUtil.exit(AppCluster.class, null, Configs.minaServerConfig_http);
            return null;
        }

        ThreadPoolExecutorConfig threadExecutorConfig_http =
                YamlUtil.read(
                        path + Configs.threadExecutorConfig_http, ThreadPoolExecutorConfig.class);
        if (threadExecutorConfig_http == null) {
            SysUtil.exit(AppCluster.class, null, Configs.threadExecutorConfig_http);
            return null;
        }

        NetPort.CLUSTER_HTTP_PORT = minaServerConfig_http.getHttpPort();
        clusterHttpServer = new ClusterHttpServer(threadExecutorConfig_http, minaServerConfig_http);


        // tcp server
        JedisClusterConfig jedisClusterConfig =
                YamlUtil.read(path + Configs.jedisClusterConfig, JedisClusterConfig.class);
        if (jedisClusterConfig == null) {
            SysUtil.exit(AppCluster.class, null, Configs.jedisClusterConfig);
        }

        ThreadPoolExecutorConfig threadExecutorConfig_tcp =
                YamlUtil.read(path + Configs.threadExecutorConfig_tcp, ThreadPoolExecutorConfig.class);
        if (threadExecutorConfig_tcp == null) {
            SysUtil.exit(AppCluster.class, null, Configs.threadExecutorConfig_tcp);
        }
        clusterTcpServer = new ClusterTcpServer(threadExecutorConfig_tcp, minaServerConfig_tcp);
        return clusterServer;
    }

    @Override
    public void run() {
        // mainServerThread.addTimerEvent(new ServerHeartTimer());

        // 启动mina相关
        log.info("注册中心http服务器启动成功,listen to [{}]...", clusterHttpServer.getHttpPort());
        new Thread(clusterHttpServer).start();

        log.info("注册中心TCP服务器启动成功,listen to [{}]...", clusterTcpServer.getPort());
        new Thread(clusterTcpServer).start();

//		ScriptManager.getInstance().init("",(str) -> SysUtil.exit(this.getClass(), null, "加载脚本错误"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            log.error("", ex);
        }
    }


}
