package info.xiaomo.server.server;


import info.xiaomo.core.concurrent.IQueueDriverCommand;

public interface MessageProcessor {
	
	void process(IQueueDriverCommand message);

}
