package info.xiaomo.server.server;


import info.xiaomo.core.concurrent.IQueueDriverCommand;

public interface MessageProcessor {
	
	public void process(IQueueDriverCommand message);

}
