package info.xiaomo.server.server;


import info.xiaomo.core.concurrent.IQueueCommand;

public interface MessageProcessor {
	
	void process(IQueueCommand message);

}
