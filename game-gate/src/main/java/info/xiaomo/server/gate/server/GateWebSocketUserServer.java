package info.xiaomo.server.gate.server;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.network.mina.TcpServer;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.websocket.WebSocketCodecFactory;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.server.GameService;
import info.xiaomo.server.gate.script.IGateServerScript;
import info.xiaomo.server.gate.server.handler.GateWebSocketUserServerHandler;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.firewall.BlacklistFilter;

/**
 * 网关 用户WebSocket 服务器（网页链接）
 *
 *
 * 2017年9月19日 下午1:37:02
 */
public class GateWebSocketUserServer extends GameService<MinaServerConfig> {
	private final TcpServer tcpServer;
	private final MinaServerConfig minaServerConfig;
	final Map<String, IoFilter> filters=new HashMap<>();
	private final BlacklistFilter blacklistFilter;	//IP黑名单过滤器
	private final GateWebSocketUserServerHandler gateWebSocketUserServerHandler;

	public GateWebSocketUserServer(MinaServerConfig minaServerConfig) {
		super(null);
		this.minaServerConfig=minaServerConfig;
		blacklistFilter=new BlacklistFilter();
		filters.put("Blacklist", blacklistFilter);
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGateServerScript.class, script->script.setIpBlackList(blacklistFilter));
		gateWebSocketUserServerHandler=new GateWebSocketUserServerHandler();
		tcpServer=new TcpServer(minaServerConfig, gateWebSocketUserServerHandler, new WebSocketCodecFactory(), filters);
	}

	@Override
	protected void running() {
		gateWebSocketUserServerHandler.setService(this);
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
