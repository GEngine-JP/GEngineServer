package info.xiaomo.server;

import info.xiaomo.core.config.annotation.ConfigFileScan;
import info.xiaomo.core.config.annotation.PackageScan;
import info.xiaomo.server.back.BackServer;
import info.xiaomo.server.server.GameContext;
import info.xiaomo.server.server.GameServer;
import info.xiaomo.server.server.ServerOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * QQ_NO: 83387856
 * Date: 2016/11/24 10:11
 * Copyright(©) 2015 by xiaomo.
 **/

@ConfigFileScan(value = "/Users/hupeng/IdeaProjects/game/ConfigData", suffix = ".xlsx")
@PackageScan("info.xiaomo.server.config.beans")
public class GameServerBootstrap {
    public static final Logger LOGGER = LoggerFactory.getLogger(GameServerBootstrap.class);

    public static void main(String[] args) throws Exception {
        String configPath = "config.properties";
        if (args.length > 0) {
            configPath = args[0];
        }
        ServerOption option = new ServerOption(configPath);
        GameContext.init(option);

        GameServer server = GameContext.createGameServer();
        server.start();
        LOGGER.warn("游戏服务器启动成功...");

        BackServer backServer = GameContext.createBackServer();
        backServer.start();
        LOGGER.warn("后台服务器启动成功...");

    }
}