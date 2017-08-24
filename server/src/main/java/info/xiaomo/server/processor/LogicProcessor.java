package info.xiaomo.server.processor;


import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;
import info.xiaomo.server.server.MessageProcessor;
import info.xiaomo.server.server.Session;
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
public class LogicProcessor implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogicProcessor.class);
    private Executor executor = Executors.newSingleThreadExecutor(r -> new Thread(r, ""));

    @Override
    public void process(IQueueDriverCommand message) {

        Session session = (Session) message.getParam();
        if (session == null) {
            LOGGER.error("找不到session");
            return;
        }
        this.executor.execute(message);
    }

}
