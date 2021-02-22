package info.xiaomo.server.rpg.system.gm;

import info.xiaomo.server.rpg.entify.User;
import info.xiaomo.server.rpg.server.Session;
import info.xiaomo.server.rpg.system.gm.command.Gm;
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
 * Date  : 2017/7/13 15:05
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class GmManager {

	public static final Logger LOGGER = LoggerFactory.getLogger(GmManager.class);

	private static GmManager ourInstance = new GmManager();

	public static GmManager getInstance() {
		return ourInstance;
	}

	private GmManager() {
	}

	public void execGMCmdFromGame(Session session, String command) {

		User user = session.getUser();
		if (user == null) {
			return;
		}

		int gmLevel = user.getGmLevel();

		LOGGER.warn("玩家:{} 执行gm命令: {}", session.getUser().getLoginName(), command);
		String ret = execGmCmd(session, command, gmLevel);
		//发送到聊天频道
//            ResGMMessage msg = new ResGMMessage();
//            msg.setContent(ret);
//            MessageUtil.sendMsg(msg, session.getUser().getId());
	}

	private String execGmCmd(Session session, String gmStr, int gmLevel) {


		gmStr = gmStr.substring(1);
		String[] commandArray = gmStr.split(" ");

		GMCommand command = GMCommand.getCommand(commandArray[0]);

		if (command == null) {
			return "没有对应GM命令";
		}

		if (command.getGmLevel() > gmLevel) {
			return "你没有权限执行此GM命令";
		}

		Gm gm = GMCommand.getGm(command);

		String ret = gm.executeGM(session, commandArray);

		if (ret != null) {
			return ret;
		}

		return "执行成功";
	}
}
