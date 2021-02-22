package info.xiaomo.server.rpg.server;

import info.xiaomo.gengine.network.IService;
import info.xiaomo.gengine.network.NetworkServiceBuilder;
import info.xiaomo.server.rpg.config.ConfigDataManager;
import info.xiaomo.server.rpg.constant.GameConst;
import info.xiaomo.server.rpg.db.DataCenter;
import info.xiaomo.server.rpg.event.EventRegister;
import info.xiaomo.server.rpg.processor.LogicProcessor;
import info.xiaomo.server.rpg.processor.LoginProcessor;
import info.xiaomo.server.rpg.system.schedule.ScheduleManager;
import lombok.Data;

/**
 * Copyright(©) 2017 by xiaomo.
 */
@Data
public class GameServer {

	private IService networkService;

	private boolean networkState = false;

	private MessageRouter router;

	public GameServer() throws Exception {
		int bossLoopGroupCount = 1;
		int workerLoopGroupCount = Math.max(Runtime.getRuntime().availableProcessors(), 4);

		GameMessageAndHandlerPool pool = new GameMessageAndHandlerPool();

		router = new MessageRouter(pool);
		NetworkServiceBuilder builder = new NetworkServiceBuilder();
		builder.setImessageandhandler(pool);
		builder.setBossLoopGroupCount(bossLoopGroupCount);
		builder.setWorkerLoopGroupCount(workerLoopGroupCount);
		builder.setPort(GameContext.getGameServerPort());
		builder.setListener(new NetworkListener());
		builder.setConsumer(router);

		//登录和下线
		router.registerProcessor(GameConst.QueueId.LOGIN_LOGOUT, new LoginProcessor());
		//业务队列
		router.registerProcessor(GameConst.QueueId.LOGIC, new LogicProcessor());

		// 创建网络服务
		networkService = builder.createService();

		// 初始化数据库
		DataCenter.init();

		//初始化配置文件
		ConfigDataManager.getInstance().init();

		// 注册事件
		EventRegister.registerPreparedListeners();

		//开启定时任务
		ScheduleManager.getInstance().start();
	}

	public MessageRouter getRouter() {
		return this.router;
	}


	public void start() {
		networkService.start();
		if (networkService.isOpened()) {
			networkState = true;
		}
	}

	public boolean isOpen() {
		return networkState;
	}

	public void stop() {
		networkService.stop();
		networkState = false;
	}


}
