package info.xiaomo.core.client;


import info.xiaomo.core.net.MessagePool;
import info.xiaomo.core.net.NetworkConsumer;
import info.xiaomo.core.net.NetworkEventListener;

public class ClientBuilder {

	protected String host;

	protected int port;

	protected MessagePool messagePool;

	private NetworkEventListener listener;

	private NetworkConsumer consumer;

	private boolean pooled;

	private int poolSize;

	public Client build() {

		if (this.pooled) {
			if (this.poolSize <= 0) {
				this.poolSize = 8;
			}
			return new PooledClient(this);
		} else {
			return new Client(this);
		}

	}

	public boolean isPooled() {
		return pooled;
	}

	public void setPooled(boolean pooled) {
		this.pooled = pooled;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MessagePool getMessagePool() {
		return messagePool;
	}

	public void setMessagePool(MessagePool messagePool) {
		this.messagePool = messagePool;
	}

	public NetworkEventListener getListener() {
		return listener;
	}

	public void setListener(NetworkEventListener listener) {
		this.listener = listener;
	}

	public NetworkConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(NetworkConsumer consumer) {
		this.consumer = consumer;
	}

}
