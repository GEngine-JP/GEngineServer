package info.xiaomo.core.client;

import info.xiaomo.core.net.Message;
import info.xiaomo.core.net.MessageDecoder;
import info.xiaomo.core.net.MessageEncoder;
import info.xiaomo.core.net.MessageGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/** 
 * 
 * 基于netty和当前消息结构的一个客户端连接器
 * @author xiaomo
 * 2017年7月15日 下午10:25:01
 */
public class Client {

	
	private short sequence = 0;
	private final Object seq_lock = new Object();
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

	protected ClientBuilder builder;

	protected Channel channel;

	protected Bootstrap bootstrap;

	protected EventLoopGroup group;

	protected Map<Short, ClientFuture<Message>> futureMap = new ConcurrentHashMap<>();
	
	public Client(final ClientBuilder builder) {
		this.builder = builder;

		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pip = ch.pipeline();
				pip.addLast("NettyMessageDecoder", new MessageDecoder(builder.getMessagePool()));
				pip.addLast("NettyMessageEncoder", new MessageEncoder());
				pip.addLast("NettyMessageExecutor", new ClientMessageExecutor(builder.getConsumer(), builder.getListener(), futureMap));
			}
		});

	}

	public boolean sendMsg(List<Message> list) {
		try {
			Channel channel = getChannel(Thread.currentThread().getId());
			if (channel != null) {
				MessageGroup group = new MessageGroup();
				for (Message message : list) {
					group.addMessage(message);
				}
				channel.writeAndFlush(group);
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("消息发送失败.", e);
		}
		return false;
	}

	public boolean sendMsg(Message message) {
		Channel channel = getChannel(Thread.currentThread().getId());
		if (channel != null && channel.isOpen()) {
			channel.writeAndFlush(message);
			return true;
		}
		return false;
	}

	public Message sendSyncMsg(Message message) {
		return this.sendSyncMsg(message, 200000);
	}

	public Message sendSyncMsg(Message message, final long timeout) {
		message.setSequence(getSequence());
		try {
			Channel channel = getChannel(Thread.currentThread().getId());
			channel.writeAndFlush(message);
			
			ClientFuture<Message> f = ClientFuture.create();
			futureMap.put(message.getSequence(), f);
			return f.get(timeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ClientFuture<Message> future = futureMap.remove(message.getSequence());
			if (future != null) {
				future.cancel(false);
			}
		}
		return null;
	}

	public boolean synced(Message message) {
		int msgid = message.getId();
		if (futureMap.containsKey(msgid)) {
			return false;
		}
		return true;
	}



	public void connect() throws Exception {
		ChannelFuture f = bootstrap.connect(builder.getHost(), builder.getPort()).sync();
		channel = f.channel();
	}

	public Channel getChannel(long id) {
		return this.channel;
	}

	public void stop() throws IOException {
		Future<?> cf = this.channel.close();

		try {
			cf.get(5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			LOGGER.info("Chennel关闭失败", e);
		}
		Future<?> gf = group.shutdownGracefully();
		try {
			gf.get(5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			LOGGER.info("EventLoopGroup关闭失败", e);
		}
		LOGGER.info("Netty Client on port:{} is closed", builder.getPort());
	}
	
	/**
	 * 只调用关闭连击和netty线程，快速返回，不保证一定能够执行完毕关闭逻辑
	 * @throws IOException
	 */
	public void stopQuickly() throws IOException {
		this.channel.close();
		group.shutdownGracefully();
	}
	
	public short getSequence(){
		synchronized(seq_lock){
			sequence++;
			return sequence;
		}
	}

}
