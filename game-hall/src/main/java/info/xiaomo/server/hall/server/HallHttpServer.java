package info.xiaomo.server.hall.server;

import info.xiaomo.gengine.network.handler.*;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.service.GameHttpSevice;
import info.xiaomo.gengine.script.ScriptManager;

/**
 * 大厅HTTP服务
 *
 * <p>2017年7月25日 上午11:46:07
 */
public class HallHttpServer extends GameHttpSevice {

	public HallHttpServer(MinaServerConfig minaServerConfig) {
		super(minaServerConfig);
	}

	@Override
	protected void running() {
		super.running();
		ScriptManager.getInstance().addIHandler(ReloadScriptHandler.class);
		ScriptManager.getInstance().addIHandler(CloseServerHandler.class);
		ScriptManager.getInstance().addIHandler(ReloadConfigHandler.class);
		ScriptManager.getInstance().addIHandler(JvmInfoHandler.class);
		ScriptManager.getInstance().addIHandler(GetFaviconHandler.class);
		ScriptManager.getInstance().addIHandler(ThreadInfoHandler.class);
	}
}
