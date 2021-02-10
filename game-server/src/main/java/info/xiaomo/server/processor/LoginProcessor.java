package info.xiaomo.server.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import info.xiaomo.core.concurrent.command.IQueueDriverCommand;
import info.xiaomo.core.network.netty.IProcessor;

/**
 * 登录消息处理器
 *
 * @author Administrator
 */
public class LoginProcessor implements IProcessor {

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
