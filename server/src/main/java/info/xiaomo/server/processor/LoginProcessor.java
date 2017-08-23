package info.xiaomo.server.processor;


import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;
import info.xiaomo.server.server.MessageProcessor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 登录消息处理器
 *
 * @author Administrator
 */
public class LoginProcessor implements MessageProcessor {

    private Executor executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Login"));

    @Override
    public void process(IQueueDriverCommand message) {
        this.executor.execute(message);
    }

}
