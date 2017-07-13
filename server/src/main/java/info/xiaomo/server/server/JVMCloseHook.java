package info.xiaomo.server.server;


import info.xiaomo.server.back.GameCloseThread;

public class JVMCloseHook extends Thread {

	@Override
	public void run() {
		//1、踢掉所有玩家
		//2、保存所有玩家数据
		//3、关闭所有线程池
		//4、退出
		if(!Context.isServerCloseLogicExecuted()){
			new GameCloseThread((short) 0, 4, null).run();
		}
		
	}

}
