package info.xiaomo.server.gateway.server;

import info.xiaomo.gengine.network.mina.TcpServer;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.server.gateway.server.handler.GateTcpGameServerHandler;

/**
 * 子游戏连接 服务
 *
 * <p>游戏服、大厅服等内部共用的服务器 2017年6月30日 下午2:20:01
 */
public class GateTcpGameServer extends GameService<MinaServerConfig> {
  private final TcpServer tcpServer;
  private final MinaServerConfig minaServerConfig;

  public GateTcpGameServer(MinaServerConfig minaServerConfig) {
    super(null);
    this.minaServerConfig = minaServerConfig;
    tcpServer = new TcpServer(minaServerConfig, new GateTcpGameServerHandler(this));
  }

  @Override
  protected void running() {
    tcpServer.run();
  }

  @Override
  protected void onShutdown() {
    super.onShutdown();
    tcpServer.stop();
  }

  public MinaServerConfig getMinaServerConfig() {
    return minaServerConfig;
  }
}
