package info.xiaomo.server.server;


import info.xiaomo.gameCore.base.concurrent.IQueueCommand;

public interface MessageProcessor {
	
	void process(IQueueCommand handler);

}
