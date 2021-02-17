package info.xiaomo.server.gateway.server.handler;

import java.util.Arrays;
import info.xiaomo.gengine.network.mina.config.MinaServerConfig;
import info.xiaomo.gengine.network.mina.handler.DefaultProtocolHandler;
import info.xiaomo.gengine.network.server.BaseServerConfig;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.MsgUtil;
import info.xiaomo.server.gateway.script.IGateServerScript;
import info.xiaomo.server.gateway.manager.UserSessionManager;
import info.xiaomo.server.gateway.server.GateTcpGameServer;
import info.xiaomo.server.gateway.struct.UserSession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏服、大厅服等内部共用的服务器
 * 
 *  2017年6月30日 下午2:05:46
 */
public class GateTcpGameServerHandler extends DefaultProtocolHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GateTcpGameServer.class);

	private final GameService<MinaServerConfig> service;

	public GateTcpGameServerHandler(GameService<MinaServerConfig> service) {
		super(12);
		this.service = service;
	}

	/**
	 * 奖消息转发给游戏客户端
	 */
	@Override
	protected void forward(IoSession session, int msgID, byte[] bytes) {
		long rid = MsgUtil.getMessageRID(bytes, 0);
		if (rid > 0) {
			UserSession userSession = UserSessionManager.getInstance().getUserSessionbyRoleId(rid);
			if (userSession != null) {
//				LOGGER.debug("{} bytes:{}", bytes.length, bytes);
				//udp转发
				if(userSession.getClientUdpSession()!=null){
					if(ScriptManager.getInstance().getBaseScriptEntry().predicateScripts(IGateServerScript.class, (IGateServerScript script)->script.isUdpMsg(userSession.getServerType(),msgID))){
						userSession.sendToClientUdp(Arrays.copyOfRange(bytes, 8, bytes.length));
						return;
					}
				}
				
				//tcp返回
				userSession.sendToClient(Arrays.copyOfRange(bytes, 8, bytes.length)); // 前8字节为角色ID
				return;
			}
		}

		LOGGER.warn("消息[{}]未找到角色{}", msgID, rid);
	}

	@Override
	public GameService<? extends BaseServerConfig> getService() {
		return service;
	}

	@Override
	public void sessionOpened(IoSession session) {
		super.sessionOpened(session);
	}

	@Override
	public void event(IoSession ioSession, FilterEvent filterEvent) throws Exception {

	}


}
