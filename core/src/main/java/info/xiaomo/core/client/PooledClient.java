package info.xiaomo.core.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PooledClient extends Client {

	protected Channel[] channels;

	public PooledClient(ClientBuilder builder) {
		super(builder);
		channels = new Channel[builder.getPoolSize()];
	}

	public void connect() throws IOException, InterruptedException {
		// Nothing to do.
	}

	public Channel createChannel() {
		Channel channel = null;
		try {
			ChannelFuture f = bootstrap.connect(builder.getHost(), builder.getPort()).sync();
			channel = f.channel();
		} catch (Exception e) {
			LOGGER.error("暂时不能连到：" + builder.getHost() + ":" + builder.getPort());
		}
		return channel;
	}

	public synchronized Channel getChannel(long id) {
		int i = (int) (id % builder.getPoolSize());
		Channel oldChannel = channels[i];
		if (oldChannel == null || !oldChannel.isActive()) {
			Channel newChannel = createChannel();
			channels[i] = newChannel;
			if (oldChannel != null) {
				oldChannel.close();
			}
			return newChannel;
		}
		return oldChannel;
	}

	public void stop() throws IOException {
		for (Channel channel : channels) {
			Future<?> cf = channel.close();
			try {
				cf.get(5000, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				LOGGER.info("Chennel关闭失败", e);
			}
		}

		Future<?> gf = group.shutdownGracefully();
		try {
			gf.get(5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			LOGGER.info("EventLoopGroup关闭失败", e);
		}
		LOGGER.info("Netty Client on port:{} is closed", builder.getPort());
	}
	
	public void stopQuickly() throws IOException {
		for (Channel channel : channels) {
			channel.close();
		}
		group.shutdownGracefully();
	}

}
