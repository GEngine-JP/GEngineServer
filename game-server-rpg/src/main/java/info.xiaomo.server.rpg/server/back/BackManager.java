package info.xiaomo.server.rpg.server.back;

import info.xiaomo.server.rpg.server.game.Session;
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
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/13 14:53
 * desc  : 后台管理器
 * Copyright(©) 2017 by xiaomo.
 */
public class BackManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(BackManager.class);

	private static BackManager ourInstance = new BackManager();

	public static BackManager getInstance() {
		return ourInstance;
	}

	private BackManager() {
	}

	public void closeServer() {
		LOGGER.error("通过后台命令关服");
		System.exit(0);
	}

	public void exeGM(short sequence, Session session, String command) {
//        ResGMMessage msg = new ResGMMessage();
//        msg.setSequence(sequence);
//        msg.setContent(command);
//        MessageUtil.sendMsg(msg, session.getUser().getId());

	}

	public void reloadCfg(short sequence, Session session, int type, String cfgName, String cacheName) {

	}
}
