package info.xiaomo.core.concurrent;


import info.xiaomo.core.concurrent.queue.ICommandQueue;

/**
 * 拥有一个队列的命令.</br>
 * 该命令可以放入QueueDriver中执行.
 * 
 * 
 * @author Administrator
 */
public interface IQueueDriverCommand extends ICommand {

	/**
	 * 后去队列id
	 * @return
	 */
	public int getQueueId();

	/**
	 * 设置队列id
	 * @param queueId
	 */
	public void setQueueId(int queueId);

	/**
	 * 获取所在队列
	 * @return
	 */
	public ICommandQueue<IQueueDriverCommand> getCommandQueue();

	/**
	 * 设置所在队列
	 * @param commandQueue
	 */
	public void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue);
	
	/**
	 * 获取一个额外的参数,随便存什么，具体逻辑具体使用，可以不使用该参数
	 * @return
	 */
	public Object getParam();
	
	/**
	 * 设置一个额外的参数,随便存什么，具体逻辑具体使用，可以不使用该参数
	 * @param param
	 */
	public void setParam(Object param);

}
