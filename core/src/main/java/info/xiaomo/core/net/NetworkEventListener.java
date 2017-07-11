package info.xiaomo.core.net;

import io.netty.channel.ChannelHandlerContext;

/**
 * 网络事件监听器
 * @author 张力
 */
public interface NetworkEventListener {
	
	/**
	 * 连接简历
	 */
	void onConnected(ChannelHandlerContext ctx);
	
	/**
	 * 连接断开
	 */
	void onDisconnected(ChannelHandlerContext ctx);
	
	/**
	 * 异常发生
	 */
	void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause);
	
}
