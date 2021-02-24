package info.xiaomo.server.rpg.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import info.xiaomo.gengine.concurrent.command.IQueueDriverCommand;
import info.xiaomo.gengine.network.IProcessor;

/**
 * 业务消息处理器
 *
 * @author zhangli 2017年6月6日 下午9:34:00
 */
public class LogicProcessor implements IProcessor {

    private final ExecutorService executor =
            new ThreadPoolExecutor(
                    8,
                    8,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(100000),
                    new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void process(IQueueDriverCommand handler) {
        this.executor.execute(handler);
    }
}
