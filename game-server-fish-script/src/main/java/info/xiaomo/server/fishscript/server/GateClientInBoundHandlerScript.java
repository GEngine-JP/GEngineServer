package info.xiaomo.server.fishscript.server;

import info.xiaomo.gengine.network.mina.message.IDMessage;
import info.xiaomo.gengine.network.netty.config.NettyClientConfig;
import info.xiaomo.gengine.network.netty.handler.DefaultClientInBoundHandler;
import info.xiaomo.gengine.network.netty.handler.IChannelHandlerScript;
import info.xiaomo.gengine.network.netty.service.MutilNettyTcpClientService;
import info.xiaomo.gengine.network.server.BaseServerConfig;
import info.xiaomo.gengine.network.server.GameService;
import info.xiaomo.gengine.network.server.IMutilTcpClientService;
import info.xiaomo.gengine.network.server.ServerState;
import info.xiaomo.server.fish.server.BydrServer;
import info.xiaomo.server.shared.protocol.server.GameServerInfo;
import info.xiaomo.server.shared.protocol.server.ServerRegisterRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty连接网关客户端消息处理器脚本
 * <p>
 * <p>
 * 2017年8月30日 上午10:52:05
 */
public class GateClientInBoundHandlerScript implements IChannelHandlerScript {
	private static final Logger LOGGER = LoggerFactory.getLogger(GateClientInBoundHandlerScript.class);

	/**
	 * 向网关服注册channel 连接
	 */
	@Override
	public void channelActive(Class<? extends ChannelHandler> handlerClass, GameService<? extends BaseServerConfig> service,
	                          Channel channel) {
		if (!handlerClass.isAssignableFrom(DefaultClientInBoundHandler.class)
				|| !(service instanceof MutilNettyTcpClientService)) {
			return;
		}
		// 向网关服注册session
		IMutilTcpClientService<? extends BaseServerConfig> client = BydrServer.getInstance().getBydr2GateClient();
		if (!(client instanceof MutilNettyTcpClientService)) {
			LOGGER.warn("未开启netty服务");
			return;
		}
		NettyClientConfig config = ((MutilNettyTcpClientService) client).getNettyClientConfig();
		ServerRegisterRequest.Builder builder = ServerRegisterRequest.newBuilder();
		GameServerInfo.Builder info = GameServerInfo.newBuilder();
		info.setId(config.getId());
		info.setIp("");
		info.setMaxUserCount(1000);
		info.setOnline(1);
		info.setName(config.getName());
		info.setState(ServerState.NORMAL.getState());
		info.setType(config.getType().getType());
		info.setWwwIp("");
//		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IGameServerCheckScript.class,
//				script -> script.buildServerInfo(info));
		builder.setServerInfo(info);
		channel.writeAndFlush(new IDMessage(channel, builder.build(), 0, 0));
	}

}
