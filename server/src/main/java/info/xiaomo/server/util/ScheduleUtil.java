package info.xiaomo.server.util;

import info.xiaomo.gameCore.base.concurrent.executor.AutoSubmitExecutor;
import info.xiaomo.gameCore.base.concurrent.executor.OrderedQueuePoolExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduleUtil {

	private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4,
			new ThreadFactory() {
		final AtomicInteger count = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			int curCount = count.incrementAndGet();
			return new Thread(r, "业务线程（线程池）-" + curCount);
		}
	});

	public static final ScheduledExecutorService EVENT_DISPATCHER_EXECUTOR = Executors
			.newScheduledThreadPool(4,
			new ThreadFactory() {
				final AtomicInteger count = new AtomicInteger(0);

				@Override
				public Thread newThread(Runnable r) {
					int curCount = count.incrementAndGet();
					return new Thread(r, "场景定时事件派发线程-" + curCount);
				}
			});

	public static final ScheduledExecutorService CHAT_PUSH_EXECUTOR = Executors.newScheduledThreadPool(4,
			new ThreadFactory() {
				final AtomicInteger count = new AtomicInteger(0);
				@Override
				public Thread newThread(Runnable r) {
					int curCount = count.incrementAndGet();
					return new Thread(r, "聊天监控推送线程-" + curCount);
				}
			});

	/**
	 * 游戏驱动主线程池
	 */
	public static final AutoSubmitExecutor STAGE_COMMON_DRIVER_EXECUTOR = new AutoSubmitExecutor("游戏公共驱动线程",
			Math.max(Runtime.getRuntime().availableProcessors() * 3 / 2, 12),
			Math.max(Runtime.getRuntime().availableProcessors() * 2, 16));

	/**
	 * 游戏登录线程池
	 */
	public static final OrderedQueuePoolExecutor LOGIN_EXECUTOR = new OrderedQueuePoolExecutor("登录线程",
			1, 5000);

	/**
	 * 心跳线程
	 */
	public static final OrderedQueuePoolExecutor HEARTBEAT_EXECUTOR = new OrderedQueuePoolExecutor("心跳线程",
			1, 5000);

	/**
	 * 服务器共享线程
	 */
	public static final OrderedQueuePoolExecutor SHARE_EXECUTOR = new OrderedQueuePoolExecutor("共享线程",
			1, 5000);

	/**
	 * 共享服查询帮会线程
	 */
	public static final OrderedQueuePoolExecutor LOOKSHAREUNION_EXECUTOR =  new OrderedQueuePoolExecutor("共享服查询帮会线程",
			2, 5000);


	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period,
                                                         TimeUnit unit) {
		return executor.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long period,
                                                            TimeUnit unit) {
		return executor.scheduleWithFixedDelay(command, initialDelay, period, unit);
	}

	public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return executor.schedule(command, delay, unit);
	}

	public static ScheduledExecutorService getExecutor() {
		return executor;
	}

	public static Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	public static void shutdown(ExecutorService service) {
		service.shutdown();
		int t = 120;
		try {
			while (t-- > 0 && !service.awaitTermination(1, TimeUnit.SECONDS)) ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
