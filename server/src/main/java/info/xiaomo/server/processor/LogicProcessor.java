package info.xiaomo.server.processor;


import info.xiaomo.gameCore.base.concurrent.command.IQueueDriverCommand;
import info.xiaomo.core.network.IProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 业务消息处理器
 *
 * @author zhangli
 * 2017年6月6日 下午9:34:00
 */
public class LogicProcessor implements IProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogicProcessor.class);
    private Executor executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "LOGIC"));

    @Override
    public void process(IQueueDriverCommand handler) {
        this.executor.execute(handler);
    }

}
