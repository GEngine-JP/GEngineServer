package info.xiaomo.core.client;

import info.xiaomo.core.net.message.Message;
import info.xiaomo.core.net.message.MessageExecutor;
import info.xiaomo.core.net.network.NetworkConsumer;
import info.xiaomo.core.net.network.NetworkEventListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public class ClientMessageExecutor extends MessageExecutor {

	protected Map<Short, ClientFuture<Message>> futureMap;

	public ClientMessageExecutor(NetworkConsumer consumer, NetworkEventListener listener,
								 Map<Short, ClientFuture<Message>> futureMap) {
		super(consumer, listener);
		this.futureMap = futureMap;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		Message m = (Message) msg;
		ClientFuture<Message> f = futureMap.get(m.getSequence());
		if(f != null) {
			if(!f.isCancelled()){
				f.result(m);
			}
		} else {
			super.channelRead(ctx, msg);
		}
		
	}
	
	
	
	
}
