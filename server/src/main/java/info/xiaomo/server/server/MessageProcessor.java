package info.xiaomo.server.server;


import info.xiaomo.gameCore.base.concurrent.IQueueDriverCommand;

public interface MessageProcessor {
	
	void process(IQueueDriverCommand message);

}
