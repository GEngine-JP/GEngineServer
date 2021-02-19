package info.xiaomo.server.cluster;

import java.util.Objects;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.persist.redis.jedis.JedisClusterConfig;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.utils.ConfigUtil;
import info.xiaomo.gengine.utils.FileUtil;
import info.xiaomo.gengine.utils.SysUtil;
import info.xiaomo.server.cluster.server.ClusterServer;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
 * <p>服务器注册管理中心 <br>
 * 是否可用zookeeper替换
 * <p>
 * 2017-03-31
 */
@Slf4j
public class AppCluster {

	public static String path = "";
	private static ClusterServer clusterServer;

	public static void main(String[] args) {
		path = ConfigUtil.getConfigPath();
		log.debug("configPath: {}", path);
		log.info("配置路径为：" + path);
		JedisClusterConfig jedisClusterConfig =
				FileUtil.getConfigXML(path, "jedisclusterConfig.xml", JedisClusterConfig.class);
		if (jedisClusterConfig == null) {
			SysUtil.exit(AppCluster.class, null, "jedisclusterConfig");
		}
		ThreadPoolExecutorConfig threadExecutorConfig_http =
				FileUtil.getConfigXML(
						path, "threadExecutorConfig_http.xml", ThreadPoolExecutorConfig.class);
		if (threadExecutorConfig_http == null) {
			SysUtil.exit(AppCluster.class, null, "threadExecutorConfig_http");
		}
		ThreadPoolExecutorConfig threadExecutorConfig_tcp =
				FileUtil.getConfigXML(path, "threadExecutorConfig_tcp.xml", ThreadPoolExecutorConfig.class);
		if (threadExecutorConfig_tcp == null) {
			SysUtil.exit(AppCluster.class, null, "threadExecutorConfig_tcp");
		}
		MinaServerConfig minaServerConfig_http =
				FileUtil.getConfigXML(path, "minaServerConfig_http.xml", MinaServerConfig.class);
		if (minaServerConfig_http == null) {
			SysUtil.exit(AppCluster.class, null, "minaServerConfig_http");
		}
		MinaServerConfig minaServerConfig_tcp =
				FileUtil.getConfigXML(path, "minaServerConfig_tcp.xml", MinaServerConfig.class);
		if (minaServerConfig_tcp == null) {
			SysUtil.exit(AppCluster.class, null, "minaServerConfig_tcp");
		}
		// RedisManager redisManager = new RedisManager(jedisClusterConfig);

		clusterServer =
				new ClusterServer(
						threadExecutorConfig_http,
						Objects.requireNonNull(minaServerConfig_http),
						threadExecutorConfig_tcp,
						Objects.requireNonNull(minaServerConfig_tcp));
		new Thread(clusterServer).start();
	}

	public static ClusterServer getClusterServer() {
		return clusterServer;
	}
}
