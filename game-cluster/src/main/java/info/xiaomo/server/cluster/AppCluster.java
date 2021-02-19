package info.xiaomo.server.cluster;

import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 * <p>
 * 服务器注册管理中心 <br>
 * 是否可用zookeeper替换
 * <p>
 * 2017-03-31
 */
@Slf4j
public class AppCluster {


    private static ClusterServer clusterServer;

    public static void main(String[] args) {
        clusterServer =ClusterServer.getInstance();
        new Thread(clusterServer).start();
    }

    public static ClusterServer getClusterServer() {
        return clusterServer;
    }
}
