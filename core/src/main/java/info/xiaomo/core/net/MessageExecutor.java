package info.xiaomo.core.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageExecutor extends ChannelInboundHandlerAdapter {

	private NetworkConsumer consumer;

	private NetworkEventListener listener;


	public MessageExecutor(NetworkConsumer consumer, NetworkEventListener listener) {
		super();
		this.consumer = consumer;
		this.listener = listener;
	}

	public NetworkConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(NetworkConsumer consumer) {
		this.consumer = consumer;
	}

	public NetworkEventListener getListener() {
		return listener;
	}

	public void setListener(NetworkEventListener listener) {
		this.listener = listener;
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		consumer.consume(ctx.channel(), (Message) msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		listener.onExceptionOccur(ctx, cause);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.listener.onConnected(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.listener.onDisconnected(ctx);
	}

}
