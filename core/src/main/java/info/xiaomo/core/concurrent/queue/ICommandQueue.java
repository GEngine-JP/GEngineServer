package info.xiaomo.core.concurrent.queue;

/**
 * 任务队列接口</br>
 * 所有实现该接口的队列都应该自己保证其线程安全
 * @author Administrator
 *
 * @param <V>
 */
public interface ICommandQueue<V> {

	/**
	 * 下一执行命令
	 * 
	 * @return
	 */
	public V poll();

	/**
	 * 增加执行指令
	 * 
	 * @param command
	 * @return
	 */
	public boolean offer(V value);

	/**
	 * 清理
	 */
	public void clear();

	/**
	 * 获取指令数量
	 * 
	 * @return
	 */
	public int size();

	public boolean isRunning();

	public void setRunning(boolean running);
	
	public void setName(String name);
	
	public String getName();
}
