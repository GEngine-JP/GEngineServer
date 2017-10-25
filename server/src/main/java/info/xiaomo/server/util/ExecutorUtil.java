package info.xiaomo.server.util;


import info.xiaomo.core.base.concurrent.QueueExecutor;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaomo
 */
public class ExecutorUtil {

    public static final ScheduledExecutorService COMMON_LOGIC_EXECUTOR = Executors.newScheduledThreadPool(4, new ThreadFactory() {

        AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            int curCount = count.incrementAndGet();
            return new Thread(r, "业务线程（线程池）-" + curCount);
        }
    });

    public static final ScheduledExecutorService EVENT_DISPATCHER_EXECUTOR = Executors.newScheduledThreadPool(4,
            new ThreadFactory() {
                AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    int curCount = count.incrementAndGet();
                    return new Thread(r, "场景定时事件派发线程-" + curCount);
                }
            });

    /**
     * 游戏驱动主线程池
     */
    public static final QueueExecutor COMMON_DRIVER_EXECUTOR = new QueueExecutor("游戏公共驱动线程",
            (int) (Runtime.getRuntime().availableProcessors() * 1.5) < 12 ? 12
                    : (int) (Runtime.getRuntime().availableProcessors() * 1.5),
            Runtime.getRuntime().availableProcessors() * 2 < 16 ? 16
                    : Runtime.getRuntime().availableProcessors() * 2);

    /**
     * 单独针对单独场景的驱动线程
     */
    public static final Map<Integer, QueueExecutor> SPECIAL_DRIVER_EXECUTOR_MAP = new ConcurrentHashMap<>();

    /**
     * 游戏登录线程池
     */
    public static final QueueExecutor LOGIN_EXECUTOR = new QueueExecutor("登录线程", 1, 5000);


    //===============对Executor的方法包装==========================================

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period,
                                                         TimeUnit unit) {
        return COMMON_LOGIC_EXECUTOR.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long period,
                                                            TimeUnit unit) {
        return COMMON_LOGIC_EXECUTOR.scheduleWithFixedDelay(command, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return COMMON_LOGIC_EXECUTOR.schedule(command, delay, unit);
    }

    public static Future<?> submit(Runnable task) {
        return COMMON_LOGIC_EXECUTOR.submit(task);
    }

}
