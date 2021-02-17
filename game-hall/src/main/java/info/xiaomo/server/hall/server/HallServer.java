package info.xiaomo.server.hall.server;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.mq.MQConsumer;
import info.xiaomo.gengine.network.mina.config.MinaClientConfig;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.gengine.persist.redis.jedis.JedisPubListener;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.gengine.utils.FileUtil;
import info.xiaomo.server.hall.AppHall;
import info.xiaomo.server.hall.HallChannel;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import info.xiaomo.server.shared.protocol.server.ServerRegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 大厅服务器
 *
 * <p>2017年6月28日 下午3:37:19
 */
public class HallServer implements Runnable {
  private static final Logger LOGGER = LoggerFactory.getLogger(HallServer.class);

  /** 连接网关 （接收网关转发过来的消息） */
  private final Hall2GateClient hall2GateClient;

  /** 连接集群服 （获取各服务器信息） */
  private final Hall2ClusterClient hall2ClusterClient;

  /** HTTP服务 */
  private final HallHttpServer hallHttpServer;

  /** redis订阅发布 */
  private final JedisPubListener hallPubListener;

  /** 服务器状态监测 */
  //  private final GameServerCheckTimer hallServerCheckTimer;

  /** MQ消息 */
  private final MQConsumer mqConsumer;

  public HallServer(String configPath) {

    // 加载连接大厅客户端配置
    ThreadPoolExecutorConfig hallClientThreatPool =
        FileUtil.getConfigXML(
            configPath, "hallClientThreadPoolExecutorConfig.xml", ThreadPoolExecutorConfig.class);
    if (hallClientThreatPool == null) {
      LOGGER.error("{}/hallClientThreadPoolExecutorConfig.xml未找到", configPath);
      System.exit(0);
    }
    MinaClientConfig minaClientConfig_gate =
        FileUtil.getConfigXML(configPath, "minaClientConfig_gate.xml", MinaClientConfig.class);
    if (minaClientConfig_gate == null) {
      LOGGER.error("{}/minaClientConfig_hall.xml未找到", configPath);
      System.exit(0);
    }

    // 加载连接集群配置
    MinaClientConfig minaClientConfig_cluster =
        FileUtil.getConfigXML(configPath, "minaClientConfig_cluster.xml", MinaClientConfig.class);
    if (minaClientConfig_cluster == null) {
      LOGGER.error("{}/minaClientConfig_hall.xml未找到", configPath);
      System.exit(0);
    }

    // http配置
    MinaServerConfig minaServerConfig_http =
        FileUtil.getConfigXML(configPath, "minaServerConfig_http.xml", MinaServerConfig.class);
    if (minaServerConfig_http == null) {
      LOGGER.error("{}/minaServerConfig_http.xml未找到", configPath);
      System.exit(0);
    }

    hall2GateClient = new Hall2GateClient(hallClientThreatPool, minaClientConfig_gate);
    hall2ClusterClient = new Hall2ClusterClient(minaClientConfig_cluster);

    //    hallServerCheckTimer =
    //        new GameServerCheckTimer(hall2ClusterClient, hall2GateClient, minaClientConfig_gate);

    hallHttpServer = new HallHttpServer(minaServerConfig_http);

    hallPubListener = new JedisPubListener(HallChannel.getChannels());

    mqConsumer = new MQConsumer(configPath, "hall");

    Config.SERVER_ID = minaClientConfig_gate.getId();
  }

  public static HallServer getInstance() {
    return AppHall.getBydrServer();
  }

  @Override
  public void run() {
    new Thread(hall2GateClient).start();
    new Thread(hall2ClusterClient).start();
    new Thread(hallHttpServer).start();
    //    hallServerCheckTimer.start();
    hallPubListener.start();
    // Thread thread = new Thread(mqConsumer);
    // thread.setName("MQConsumer");
    // thread.start();
  }

  public Hall2GateClient getHall2GateClient() {
    return hall2GateClient;
  }

  public Hall2ClusterClient getHall2ClusterClient() {
    return hall2ClusterClient;
  }

  public HallHttpServer getHallHttpServer() {
    return hallHttpServer;
  }

  /**
   * 构建服务器更新注册信息
   *
   * @return
   */
  public ServerRegisterRequest buildServerRegisterRequest(
      MinaClientConfig minaClientConfig) {
    ServerRegisterRequest.Builder builder =
        ServerRegisterRequest.newBuilder();
    GameServerInfo.Builder info = GameServerInfo.newBuilder();
    info.setId(minaClientConfig.getId());
    info.setIp("");
    info.setMaxUserCount(1000);
    info.setOnline(1);
    info.setName(minaClientConfig.getName());
    info.setState(ServerState.NORMAL.getState());
    info.setType(minaClientConfig.getType().getType());
    info.setWwwIp("");
    builder.setServerInfo(info);
    return builder.build();
  }
}
