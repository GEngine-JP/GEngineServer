package info.xiaomo.core.net;

import info.xiaomo.core.concurrent.IQueueDriverCommand;
import info.xiaomo.core.concurrent.queue.ICommandQueue;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MessageGroup implements Message {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageGroup.class);

	private List<Message> messageList = new ArrayList<>();

	private int length = 0;

	public void addMessage(Message message) {
		assert message != null;
		messageList.add(message);
	}


	@Override
	public byte[] encode() {

		byte[] bytes = null;

		for (Message message : messageList) {

			ByteBuf byteBuf = null;
			try {
				
				if(message.getId() == -1) {
					byteBuf = MessagePackage.packageMsgGroup(message);
				} else {
					byteBuf = MessagePackage.packageMsg(message);
				}

				int bufLength = byteBuf.readableBytes();
				if(bufLength <= 0) {
					continue;
				}
				byte[] curBytes = new byte[bufLength];
				byteBuf.getBytes(0, curBytes);

				if (bytes != null && bytes.length > 0) {
					byte[] oldBytes = bytes;
					bytes = new byte[oldBytes.length + curBytes.length];
					if (oldBytes.length > 0) {
						System.arraycopy(oldBytes, 0, bytes, 0, oldBytes.length);
					}
					System.arraycopy(curBytes, 0, bytes, oldBytes.length, curBytes.length);
				} else {
					bytes = curBytes;
				}
			} catch(Exception e){
				LOGGER.error("组消息编码错误", e);
			} finally {
				if (byteBuf != null) {
					byteBuf.release();
				}
			}
		}

		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return bytes;
	}

	@Override
	public void setQueueId(int queueId) {

	}

	@Override
	public ICommandQueue<IQueueDriverCommand> getCommandQueue() {
		return null;
	}

	@Override
	public void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue) {

	}

	@Override
	public Object getParam() {
		return null;
	}

	@Override
	public void setParam(Object param) {

	}

	@Override
	public void doAction() {

	}

	@Override
	public void run() {

	}

	@Override
	public void decode(byte[] bytes) {

	}

	@Override
	public int length() {
		return this.length;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public int getId() {
		return -1;
	}

	@Override
	public int getQueueId() {
		return 0;
	}

	@Override
	public void setSequence(short sequence) {

	}

	@Override
	public short getSequence() {
		return 0;
	}

}
