package info.xiaomo.server.cluster.server;

import info.xiaomo.core.common.utils.IntUtil;
import info.xiaomo.core.common.utils.MsgUtil;
import info.xiaomo.core.network.mina.TcpServer;
import info.xiaomo.core.network.mina.config.MinaServerConfig;
import info.xiaomo.core.network.mina.handler.DefaultProtocolHandler;
import info.xiaomo.core.server.GameService;
import info.xiaomo.core.server.ServerInfo;
import info.xiaomo.core.thread.ServerThread;
import info.xiaomo.core.thread.ThreadPoolExecutorConfig;
import info.xiaomo.core.thread.ThreadType;
import info.xiaomo.core.thread.timer.event.ServerHeartTimer;
import info.xiaomo.server.cluster.manager.ServerManager;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterTcpServer extends GameService<MinaServerConfig> {
  private static final Logger log = LoggerFactory.getLogger(ClusterTcpServer.class);

  private final TcpServer minaServer;
  private final MinaServerConfig minaServerConfig;
  public static final String SERVER_INFO = "serverInfo"; // 服务器信息

  public ClusterTcpServer(
      ThreadPoolExecutorConfig threadExecutorConfig, MinaServerConfig minaServerConfig) {
    super(threadExecutorConfig);
    this.minaServerConfig = minaServerConfig;

    minaServer = new TcpServer(minaServerConfig, new ClusterTcpServerHandler(this));
  }

  @Override
  protected void running() {
    log.debug(" run ... ");
    minaServer.run();
    ServerThread syncThread = getExecutor(ThreadType.SYNC);
    syncThread.addTimerEvent(new ServerHeartTimer());
  }

  @Override
  protected void onShutdown() {
    super.onShutdown();
    log.debug(" stop ... ");
    minaServer.stop();
  }

  @Override
  public String toString() {
    return minaServerConfig.getName();
  }

  public int getId() {
    return minaServerConfig.getId();
  }

  public String getName() {
    return minaServerConfig.getName();
  }

  public String getWeb() {
    return minaServerConfig.getChannel();
  }

  /**
   * 消息处理器
   *
   *
   * @date 2017-03-31
   */
  public class ClusterTcpServerHandler extends DefaultProtocolHandler {
    private final GameService<MinaServerConfig> service;

    public ClusterTcpServerHandler(GameService<MinaServerConfig> service) {
      super(4); // 消息ID+消息内容
      this.service = service;
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) {
      MsgUtil.close(
          ioSession,
          "链接空闲:"
              + ioSession.toString()
              + " "
              + idleStatus
                  .toString()); // 客户端长时间不发送请求，将断开链接LoginTcpServer->minaServerConfig->readerIdleTime
      // 60
      // 1分钟
    }

    @Override
    public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {}

    @Override
    public void sessionClosed(IoSession ioSession) {
      super.sessionClosed(ioSession);
      ServerInfo serverInfo = (ServerInfo) ioSession.getAttribute(SERVER_INFO);
      if (serverInfo != null) {
        log.warn("服务器:" + serverInfo.getName() + "断开链接");
        ServerManager.getInstance().removeServer(serverInfo);
      } else {
        log.error("未知游戏服务器断开链接");
      }
    }

    @Override
    protected void forward(IoSession session, int msgID, byte[] bytes) {
      log.warn("无法找到消息处理器：msgID{},bytes{}", msgID, IntUtil.BytesToStr(bytes));
    }

    @Override
    public GameService<MinaServerConfig> getService() {
      return this.service;
    }
  }
}
