package info.xiaomo.core.net;

import io.netty.channel.ChannelHandler;

import java.util.ArrayList;
import java.util.List;

public class NetworkServiceBuilder {

	/**
	 * 网络线程池线程数量
	 */
	private int bossLoopGroupCount;
	/**
	 * 工作线程池线程数量
	 */
	private int workerLoopGroupCount;
	/**
	 * 监听端口
	 */
	private int port;

	/**
	 * 消息池
	 */
	private MessagePool msgPool;

	/**
	 * 网络消费者
	 */
	private NetworkConsumer consumer;

	/**
	 * 事件监听器
	 */
	private NetworkEventListener networkEventListener;

	/**
	 * 额外的handler
	 */
	private List<ChannelHandler> channelHandlerList = new ArrayList<>();

	public NetworkService createService() {
		return new NetworkService(this);
	}

	/**
	 * 添加一个handler，该handler由外部定义.</br>
	 * 注意，所有handler都按照本方法调用顺序添加在默认handler的后面.</br>
	 * 也就是说 对于 inbound来，该方法添加的handler是最后执行的，对于outbound该方法添加的handler是最先执行的.</br>
	 * 
	 * @param handler
	 */
	public void addChannelHandler(ChannelHandler handler) {
		if (handler == null) {
			throw new NullPointerException("指定的handler为空");
		}
		channelHandlerList.add(handler);
	}

	public int getBossLoopGroupCount() {
		return bossLoopGroupCount;
	}

	public void setBossLoopGroupCount(int bossLoopGroupCount) {
		this.bossLoopGroupCount = bossLoopGroupCount;
	}

	public int getWorkerLoopGroupCount() {
		return workerLoopGroupCount;
	}

	public void setWorkerLoopGroupCount(int workerLoopGroupCount) {
		this.workerLoopGroupCount = workerLoopGroupCount;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<ChannelHandler> getChannelHandlerList() {
		return channelHandlerList;
	}

	public void setChannelHandlerList(List<ChannelHandler> channelHandlerList) {
		this.channelHandlerList = channelHandlerList;
	}

	public MessagePool getMsgPool() {
		return msgPool;
	}

	public void setMsgPool(MessagePool msgPool) {
		this.msgPool = msgPool;
	}

	public NetworkConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(NetworkConsumer consumer) {
		this.consumer = consumer;
	}

	public NetworkEventListener getNetworkEventListener() {
		return networkEventListener;
	}

	public void setNetworkEventListener(NetworkEventListener networkEventListener) {
		this.networkEventListener = networkEventListener;
	}

}
