package info.xiaomo.server.server;

import info.xiaomo.gameCore.protocol.NetworkService;
import info.xiaomo.gameCore.protocol.NetworkServiceBuilder;
import info.xiaomo.server.back.BackServer;
import info.xiaomo.server.db.DataCenter;
import info.xiaomo.server.event.EventRegister;
import info.xiaomo.server.system.schedule.ScheduleManager;
import info.xiaomo.server.util.MsgExeTimeUtil;
import info.xiaomo.server.util.PackageCountUtil;
import lombok.Data;

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
@Data
public class GameServer {

    private NetworkService netWork;

    private boolean state = false;

    private MessageRouter router;

    BackServer backServer;

    public GameServer(ServerOption option) throws Exception {
        int bossLoopGroupCount = 4;
        int workerLoopGroupCount = Runtime.getRuntime().availableProcessors() < 8 ? 8
                : Runtime.getRuntime().availableProcessors();

        MessageAndHandlerPool pool = new MessageAndHandlerPool();

        // 创建游戏世界
        router = new MessageRouter(option, pool);

        NetworkServiceBuilder builder = new NetworkServiceBuilder();
        builder.setMessagePool(pool);
        builder.setBossLoopGroupCount(bossLoopGroupCount);
        builder.setWorkerLoopGroupCount(workerLoopGroupCount);
        builder.setPort(option.getGameServerPort());
        builder.setConsumer(router);
        if (option.isDebug()) {
            builder.addChannelHandler(new PackageCountUtil.PackageCountHandler());
            PackageCountUtil.open(true);
        }
        MsgExeTimeUtil.setOpen(true);

        // 创建网络服务
        netWork = builder.createService();

        backServer = new BackServer(option);

        // 创建网络服务
        netWork = builder.createService();

        // 初始化数据库
        DataCenter.init(option);

        //初始化配置文件
        //ConfigDataManager.getInstance().init(option.getConfigDataPath());

        // 注册事件
        EventRegister.registerPreparedListeners();

        //开启定时任务
        ScheduleManager.getInstance().start();
    }

    /**
     * 开启服务器
     */
    public void start() {
        netWork.open();
        if (netWork.isOpen()) {
            state = true;
        }
    }

    public void stop() {
        netWork.close();
        state = false;
    }

    public void closeNetWork() {
        netWork.close();
    }

    public void closeBackServer() {
        backServer.stop();
    }

}
