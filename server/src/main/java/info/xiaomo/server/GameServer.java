package info.xiaomo.server;

import info.xiaomo.core.config.ConfigDataManager;
import info.xiaomo.core.net.message.MessageRouter;
import info.xiaomo.core.net.network.NetWorkService;
import info.xiaomo.core.net.network.NetWorkServiceBuilder;

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

    NetWorkService netWork;

    private boolean state = false;

    MessageRouter router;

    public GameServer(ServerOption option) {
        int bossLoopGroupCount = 4;
        int workerLoopGroupCount = Runtime.getRuntime().availableProcessors() < 8 ? 8
                : Runtime.getRuntime().availableProcessors();
        NetWorkServiceBuilder builder = new NetWorkServiceBuilder();
        builder.setMsgPool(new GameMessagePool());
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setNetworkEventListener(new EventListener());
        builder.setPort(option.getGameServerPort());


        builder.setConsumer(router);
        // 创建网络服务
        netWork = builder.createService();

        ConfigDataManager.getInstance().init(option.getConfigDataPath());
    }

    /**
     * 开启服务器
     */
    public void start() {
        netWork.start();
        if(netWork.isRunning()){
            state = true;
        }
    }
}
