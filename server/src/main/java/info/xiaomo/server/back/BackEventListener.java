package info.xiaomo.server.back;

import info.xiaomo.gameCore.protocol.NetworkEventListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackEventListener implements NetworkEventListener{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BackEventListener.class);

	@Override
	public void onConnected(ChannelHandlerContext ctx) {
		LOGGER.info("back server 收到一个连接：" + ctx);
	}

	@Override
	public void onDisconnected(ChannelHandlerContext ctx) {
		LOGGER.info("back server 断开一个连接：" + ctx);
	}

	@Override
	public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.error("back server 发生错误：" + ctx, cause);
	}

}
