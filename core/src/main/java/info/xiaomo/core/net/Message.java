package info.xiaomo.core.net;


import info.xiaomo.core.concurrent.IQueueDriverCommand;

/**
 * 网络请求的消息，该消息继承了队列执行命令接口，可以直接放入QueueDriver中执行
 * @author 张力
 *
 */
public interface Message extends IQueueDriverCommand {
	
	public void decode(byte[] bytes);
	
	public byte[] encode();
	
	public int length();
	
	public void setLength(int length);
	
	public int getId();
	
	public int getQueueId();
	
	public void setSequence(short sequence);
	
	public short getSequence();
	
	
}
