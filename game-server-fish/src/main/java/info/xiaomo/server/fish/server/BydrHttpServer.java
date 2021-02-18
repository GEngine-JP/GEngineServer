package info.xiaomo.server.fish.server;


import info.xiaomo.gengine.network.handler.*;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.service.GameHttpSevice;
import info.xiaomo.gengine.script.ScriptManager;

/**
 * http服务器
 * <p>
 * <p>
 * Role> 2017年7月24日 下午1:46:00
 */
public class BydrHttpServer extends GameHttpSevice {

	public BydrHttpServer(MinaServerConfig minaServerConfig) {
		super(minaServerConfig);
	}

	@Override
	protected void running() {
		super.running();
		// 添加关服、脚本加载 等公用 handler
		ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
		ScriptManager.getInstance().addIHandler(CloseServerHandler.class);
		ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
		ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
		ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
		ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
	}

}
