package info.xiaomo.server.gameserver.gateway.server;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.service.ClientServerService;
import info.xiaomo.gengine.thread.ThreadPoolExecutorConfig;
import info.xiaomo.server.gameserver.gateway.manager.UserSessionManager;
import info.xiaomo.server.gameserver.gateway.server.handler.GateTcpUserServerHandler;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;

/**
 * 网关 用户 TCP服务器
 *
 * @date 2017-03-30
 */
public class GateTcpUserServer extends ClientServerService {
  private static final Map<String, IoFilter> filters = new HashMap<>();
  private static final BlacklistFilter blacklistFilter = new BlacklistFilter(); // IP黑名单过滤器

  static {
    filters.put("Blacklist", blacklistFilter);

    // //添加ssl支持
    // if (USE_SSL) {
    // //ssl是串行执行，加上加解密，速度很慢
    // SslFilter sslFilter = new SslFilter(GateSslContextFactory.getInstance(true));
    // filters.put("SSL", sslFilter);
    // }
  }

  public GateTcpUserServer(
      ThreadPoolExecutorConfig threadExecutorConfig, MinaServerConfig minaServerConfig) {
    super(threadExecutorConfig, minaServerConfig, new GateTcpUserServerHandler(), filters);
  }

  public static BlacklistFilter getBlacklistFilter() {
    return blacklistFilter;
  }

  @Override
  protected void onShutdown() {
    UserSessionManager.getInstance().onShutdown();
    super.onShutdown();
  }
}
