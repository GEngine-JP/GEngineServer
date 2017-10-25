package info.xiaomo.chessgame.log.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaomo
 */
public class TaskUtil {

	private static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = new ScheduledThreadPoolExecutor(15, new ThreadFactory() {
		private int count = 0;

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "日志服业务线程（线程池）" + (++count));
		}
	});

	public static void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {

		SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(task, initialDelay, period, unit);
	}

//	public static void main(String[] args) {
//		byte[] bytes = {0, 0, 1, 22};
//		Input input = KryoHelper.getInput();
//		input.setBuffer(bytes);
//	System.out.println(input.readInt());
//	}
}
