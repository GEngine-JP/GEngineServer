package info.xiaomo.core.concurrent;


import info.xiaomo.core.concurrent.queue.ICommandQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 用于执行具体业务逻辑的工作类，该类的实例中包括一个CommandQueue
 * @author Administrator
 */
public abstract class AbstractCommand implements IQueueDriverCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommand.class);

    private ICommandQueue<IQueueDriverCommand> commandQueue;
    
    /**
     * 消息所属队列ID（有可能是场景队列，也有可能是其他队列）
     */
    protected int queueId;
    
	@Override
	public void run() {
		try {
			long time = System.currentTimeMillis();
			doAction();
			long exeTime = System.currentTimeMillis() - time;
			if(exeTime > 2) {
				//LOGGER.error(this.getClass().getSimpleName() + "执行耗时:" + exeTime);
			}
				
		} catch (Throwable e) {
			LOGGER.error("命令执行错误", e);
		}
	}

	public ICommandQueue<IQueueDriverCommand> getCommandQueue() {
		return commandQueue;
	}

	public void setCommandQueue(ICommandQueue<IQueueDriverCommand> commandQueue) {
		this.commandQueue = commandQueue;
	}

	public int getQueueId() {
		return queueId;
	}

	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}

	@Override
	public Object getParam() {
		return null;
	}

	@Override
	public void setParam(Object param) {
		
	}
	
   
}
