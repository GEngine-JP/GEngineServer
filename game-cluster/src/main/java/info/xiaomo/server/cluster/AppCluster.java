package info.xiaomo.server.cluster;

import java.io.File;
import java.util.Objects;
import info.xiaomo.gengine.common.utils.FileUtil;
import info.xiaomo.gengine.common.utils.SysUtil;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.persist.redis.jedis.JedisClusterConfig;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.server.cluster.server.ClusterServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动类
 *
 * <p>服务器注册管理中心 <br>
 * 是否可用zookeeper替换
 *
 * @date 2017-03-31
 */
public class AppCluster {
  private static final Logger log = LoggerFactory.getLogger(AppCluster.class);

  private static ClusterServer clusterServer;
  public static String path = "";

  public static void main(String[] args) {
    File file = new File(System.getProperty("user.dir"));
    log.debug("user.dir: {}", System.getProperty("user.dir"));
    if ("target".equals(file.getName())) {
      path = file.getPath() + File.separatorChar + "config";
    } else {
      path = file.getPath() + File.separatorChar + "target" + File.separatorChar + "config";
    }
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
