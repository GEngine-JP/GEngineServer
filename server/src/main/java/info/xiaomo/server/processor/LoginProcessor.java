package info.xiaomo.server.processor;


import info.xiaomo.core.base.concurrent.command.IQueueDriverCommand;
import info.xiaomo.core.network.IProcessor;

import java.util.concurrent.*;

/**
 * 登录消息处理器
 *
 * @author Administrator
 */
public class LoginProcessor implements IProcessor {

    private ExecutorService executor = new ThreadPoolExecutor(8, 8, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void process(IQueueDriverCommand handler) {
        this.executor.execute(handler);
    }

}
