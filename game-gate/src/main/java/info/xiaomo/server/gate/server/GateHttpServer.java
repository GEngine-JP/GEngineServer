package info.xiaomo.server.gate.server;

import info.xiaomo.core.common.handler.*;
import info.xiaomo.core.network.mina.config.MinaServerConfig;
import info.xiaomo.core.network.mina.service.GameHttpSevice;
import info.xiaomo.core.script.ScriptManager;

/**
 * http服务器
 *
 * <p>2017年7月24日 下午1:46:00
 */
public class GateHttpServer extends GameHttpSevice {

  public GateHttpServer(MinaServerConfig minaServerConfig) {
    super(minaServerConfig);
  }

  @Override
  protected void running() {
    super.running();
    // 添加关服、脚本加载 等公用 handler
    ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
    ScriptManager.getInstance().addIHandler(ExitServerHandler.class);
    ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
    ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
    ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
    ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
  }
}
