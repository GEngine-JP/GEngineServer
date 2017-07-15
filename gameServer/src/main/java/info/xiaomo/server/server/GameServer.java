package info.xiaomo.server.server;

import info.xiaomo.core.config.ConfigDataManager;
import info.xiaomo.core.net.NetworkService;
import info.xiaomo.core.net.NetworkServiceBuilder;
import info.xiaomo.server.db.DbData;
import info.xiaomo.server.event.EventRegister;
import info.xiaomo.server.processor.LoginAndLogoutProcessor;
import info.xiaomo.server.system.schedule.ScheduleManager;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 15:08
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class GameServer {

    private NetworkService netWork;

    private boolean state = false;

    private MessageRouter router;

    public GameServer(ServerOption option) throws Exception {
        int bossLoopGroupCount = 4;
        int workerLoopGroupCount = Runtime.getRuntime().availableProcessors() < 8 ? 8
                : Runtime.getRuntime().availableProcessors();
        NetworkServiceBuilder builder = new NetworkServiceBuilder();
        builder.setMsgPool(new GameMessagePool());
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setNetworkEventListener(new EventListener());
        builder.setPort(option.getGameServerPort());

        router = new MessageRouter();
        router.registerProcessor(1, new LoginAndLogoutProcessor());

        builder.setConsumer(router);
        // 创建网络服务
        netWork = builder.createService();

        // 初始化数据库
        DbData.init(option);

        ConfigDataManager.getInstance().init(option.getConfigDataPath());

        // 注册事件
        EventRegister.registerPreparedListeners();

        //开启定时任务
        ScheduleManager.getInstance().start();
    }

    /**
     * 开启服务器
     */
    public void start() {
        netWork.start();
        if (netWork.isRunning()) {
            state = true;
        }
    }

    public void stop() {
        netWork.stop();
        state = false;
    }

    public NetworkService getNetWork() {
        return netWork;
    }

    public void setNetWork(NetworkService netWork) {
        this.netWork = netWork;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public MessageRouter getRouter() {
        return router;
    }

    public void setRouter(MessageRouter router) {
        this.router = router;
    }
}
