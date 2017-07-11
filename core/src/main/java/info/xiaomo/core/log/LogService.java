package info.xiaomo.core.log;

import info.xiaomo.core.db.jdbc.ConnectionPool;
import info.xiaomo.core.db.jdbc.DruidConnectionPool;
import info.xiaomo.core.db.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LogService {

	private static Logger LOGGER = LoggerFactory.getLogger(LogService.class);
	
	private static  LogService INSTANCE = null;
	
	static JdbcTemplate template;
	
	
	private static int coreThreadPoolSize;
	private static int maximumThreadPoolSize;

	private final ThreadPoolExecutor executor;
	
	private LogService(){
		executor = new ThreadPoolExecutor(coreThreadPoolSize, maximumThreadPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
			        new LogThreadFactory(), new LogRejectedExecutionHandler());
	}

	
	public static void init(String dsConfig, int coreThreadPoolSize, int maximumThreadPoolSize) throws Exception {
		LogService.coreThreadPoolSize = coreThreadPoolSize;
		LogService.maximumThreadPoolSize = maximumThreadPoolSize;
		ConnectionPool pool = new DruidConnectionPool(dsConfig);
		LogService.template = new JdbcTemplate(pool);
		
		Set<Class<AbstractLog>> ret = LogBeanUtil.getSubClasses("com.sh.game", AbstractLog.class);
		for (Class<AbstractLog> logClass : ret) {
			AbstractLog log = logClass.newInstance();
			log.init();
		}
		INSTANCE = new LogService();
	}
	
	/**
	 * 提交一个日志
	 * 
	 * @param log
	 */
	public static void submit(AbstractLog log) {
		INSTANCE.execute(log);
	}
	

	private void execute(AbstractLog log) {
		executor.execute(log);
	}

	static class LogRejectedExecutionHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			AbstractLog log = (AbstractLog) r;
			if(r == null){
				return;
			}
			LOGGER.error("丢弃日志提交请求,sql:" + log.buildInsertSQL() + ",params:" + Arrays.toString(log.buildInsertParam()));
		}

	}

	static class LogThreadFactory implements ThreadFactory {

		AtomicInteger count = new AtomicInteger(0);
		@Override
		public Thread newThread(Runnable r) {
			int curCount = count.incrementAndGet();
			return new Thread(r, "日志线程-" + curCount);
		}

	}

	public static void main(String[] args) throws Exception {
		LogService.init("logds.properties", 2, 4);
		ItemLog log = new ItemLog();

		log.setId(2);
		log.setItemId(32);
		log.setName("还魂丹");
		log.setNum(12);
		log.setServerId(1);
		log.setTime(System.currentTimeMillis()/ 1000);
		log.setAction("自动使用");
		
		LogService.submit(log);
		
		while(true){
			try {
	            Thread.sleep(100000);
            } catch (InterruptedException e) {
	            e.printStackTrace();
            }
		}
	}
}
