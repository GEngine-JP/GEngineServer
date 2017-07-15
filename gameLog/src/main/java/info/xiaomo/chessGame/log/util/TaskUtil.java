package info.xiaomo.chessGame.log.util;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TaskUtil {
	
	private static final ScheduledThreadPoolExecutor schedulePools = new ScheduledThreadPoolExecutor(15, new ThreadFactory() {
		private int count = 0;

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "日志服业务线程（线程池）" + (++count));
		}
	});

	public static void scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
	    
		schedulePools.scheduleAtFixedRate(task, initialDelay, period, unit);
    }
	
//	public static void main(String[] args) {
//		byte[] bytes = {0, 0, 1, 22};
//		Input input = KryoHelper.getInput();
//		input.setBuffer(bytes);
//	System.out.println(input.readInt());
//	}
}
