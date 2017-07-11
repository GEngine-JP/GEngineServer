package info.xiaomo.core.concurrent;

import info.xiaomo.core.concurrent.queue.ICommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 队列执行器</br>
 * 该executor执行完毕一个任务的时候，会自动从该任务所属队列中获取下一个任务执行，直到队列为空
 * 
 * @author 张力
 * @date 2015-3-11 下午10:51:20
 *
 */
public class QueueExecutor extends ThreadPoolExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueExecutor.class);

	/**
	 * 执行器名称
	 */
	private String name;

	/**
	 * 最小线程数
	 */
	private int corePoolSize;

	/**
	 * 最大线程数
	 */
	private int maxPoolSize;

	public QueueExecutor(final String name, int corePoolSize, int maxPoolSize) {

		super(corePoolSize, maxPoolSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
				new ThreadFactory() {
					AtomicInteger count = new AtomicInteger(0);

					@Override
					public Thread newThread(Runnable r) {
						int curCount = count.incrementAndGet();
						LOGGER.error("创建线程:" + name + "-" + curCount);
						return new Thread(r, name + "-" + curCount);
					}
				});
		this.name = name;
		this.corePoolSize = corePoolSize;
		this.maxPoolSize = maxPoolSize;
	}

	/**
	 * 指定的任务执行完毕后，调用该方法
	 * 
	 * @param task
	 *            执行的任务
	 * @param throwable
	 *            异常
	 */
	@Override
	protected void afterExecute(Runnable task, Throwable throwable) {
		
		super.afterExecute(task, throwable);
		IQueueDriverCommand work = (IQueueDriverCommand) task;
		ICommandQueue<IQueueDriverCommand> queue = work.getCommandQueue();
		synchronized (queue) {
			IQueueDriverCommand nextCommand = queue.poll();
			if (nextCommand == null) {
				// 执行完毕后如果队列中没有任务了，那么设置运行标记为false
				queue.setRunning(false);
				
				
			} else {
				// 执行完毕后如果队列中还有任务，那么继续执行下一个
				execute(nextCommand);
				//LOGGER.error("存在任务，继续执行任务");
			}
		}
	}

	public String getName() {
		return name;
	}

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

}
