package info.xiaomo.server;

import info.xiaomo.server.back.BackServer;
import info.xiaomo.server.server.Context;
import info.xiaomo.server.server.GameServer;
import info.xiaomo.server.server.JVMCloseHook;
import info.xiaomo.server.server.ServerOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * QQ_NO: 83387856
 * Date: 2016/11/24 10:11
 * Copyright(©) 2015 by xiaomo.
 **/

public class GameServerBootstrap {
    public static final Logger LOGGER = LoggerFactory.getLogger(GameServerBootstrap.class);

    public static void main(String[] args) throws IOException, ParseException {
        ServerOption option = new ServerOption(args[0]);
        Context.init(option);

        GameServer server = Context.createServer();
        server.start();
        LOGGER.warn("游戏服务器启动成功...");

        BackServer backServer = Context.createBackServer();
        backServer.start();
        LOGGER.warn("后台服务器启动成功...");

        Runtime.getRuntime().addShutdownHook(new JVMCloseHook());
    }
}