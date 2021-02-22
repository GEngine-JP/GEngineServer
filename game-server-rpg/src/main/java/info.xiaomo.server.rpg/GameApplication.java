package info.xiaomo.server.rpg;

import info.xiaomo.gengine.config.ConfigSuffix;
import info.xiaomo.gengine.config.annotation.ConfigFileScan;
import info.xiaomo.gengine.config.annotation.PackageScan;
import info.xiaomo.gengine.utils.PathUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.rpg.back.BackServer;
import info.xiaomo.server.rpg.server.GameContext;
import info.xiaomo.server.rpg.server.GameServer;
import info.xiaomo.server.rpg.server.ServerOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email: xiaomo@xiaomo.info
 * QQ_NO: 83387856
 * Date: 2016/11/24 10:11
 * Copyright(©) 2015 by xiaomo.
 **/

@ConfigFileScan(value = "/Users/ctwdevops/Workspace/game/GameTables", suffix = ConfigSuffix.excel)
@PackageScan("info.xiaomo.server.config.tables")
public class GameApplication {
	public static final Logger LOGGER = LoggerFactory.getLogger(GameApplication.class);

	public static void main(String[] args) {
		ServerOption option = YamlUtil.read(PathUtil.getConfigPath() + "config.yml", ServerOption.class);
		GameContext.init(option);

		GameServer server = GameContext.createGameServer();
		server.start();
		LOGGER.warn("游戏服务器启动成功...");

		BackServer backServer = GameContext.createBackServer();
		backServer.start();
		LOGGER.warn("后台服务器启动成功...");

	}
}