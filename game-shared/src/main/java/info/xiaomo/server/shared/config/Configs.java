package info.xiaomo.server.shared.config;

public interface Configs {
    String gameConfig = "gameConfig.yml";

    String minaClientConfig_cluster = "minaClientConfig_cluster.yml";
    String minaClientConfig_gate = "minaClientConfig_gate.yml";
    String minaServerConfig = "minaServerConfig.yml";
    String minaServerConfig_game = "minaServerConfig_game.yml";
    String minaServerConfig_http = "minaServerConfig_http.yml";
    String minaServerConfig_udp_user = "minaServerConfig_udp_user.yml";
    String minaServerConfig_user = "minaServerConfig_user.yml";
    String minaServerConfig_tcp = "minaServerConfig_tcp.yml";

    String minaServerConfig_websocket_user = "minaServerConfig_websocket_user.yml";
    String nettyClientConfig_cluster = "nettyClientConfig_cluster.yml";
    String nettyClientConfig_gate = "nettyClientConfig_gate.yml";

    String mqConfig = "mqConfig.yml";
    String mongoClientConfig = "mongoClientConfig.yml";
    String jedisClusterConfig = "jedisClusterConfig.yml";
    String redissonClusterConfig = "redissonClusterConfig.yml";

    String hallClientThreadPoolExecutorConfig = "hallClientThreadPoolExecutorConfig.yml";
    String threadPoolExecutorConfig = "threadPoolExecutorConfig.yml";
    String threadExecutorConfig_http = "threadExecutorConfig_http.yml";
    String threadExecutorConfig_tcp = "threadExecutorConfig_tcp.yml";
}
