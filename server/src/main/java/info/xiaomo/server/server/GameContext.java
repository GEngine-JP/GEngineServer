package info.xiaomo.server.server;

import info.xiaomo.server.back.BackServer;
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
 * Date  : 2017/7/11 15:10
 * desc  : 服务器上下文
 * Copyright(©) 2017 by xiaomo.
 */
@Data
public class GameContext {
    private static final GameContext INSTANCE = new GameContext();
    /**
     * 服务器配置
     */
    private ServerOption serverOption;

    /**
     * 开服当天凌晨0点时间戳
     */
    private long openDayZeroTime;

    private MessageRouter router;


    /**
     * 合服日期当天凌晨0点时间戳
     */
    private long combineDayZeroTime;

    private boolean combined = false;

    private GameServer gameServer;

    private BackServer backServer;

    /**
     * 服务器关闭逻辑已经是否已经执行
     */
    public boolean serverCloseLogicExecuted;

    /**
     * 游戏服务器关闭
     */
    private boolean closed;


    public static GameContext getInstance() {
        return INSTANCE;
    }

    public void init(ServerOption serverOption) {
        this.serverOption = serverOption;
    }

    public GameServer createServer() throws Exception {
        gameServer = new GameServer(serverOption);
        return gameServer;
    }

    public BackServer createBackServer() {
        backServer = new BackServer(serverOption);
        return backServer;
    }

    public ServerOption getServerOption() {
        return serverOption;
    }

    public void closeAllConnection() {
        router.closeAllConnection();
    }

    public void closeWorld() {
        router.close();
    }

    public void openWorld() {
        router.open();
    }

    public void closeBackServer() {
        backServer.stop();
    }
}
