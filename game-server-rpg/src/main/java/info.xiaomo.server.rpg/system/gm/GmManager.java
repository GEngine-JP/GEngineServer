package info.xiaomo.server.rpg.system.gm;

import info.xiaomo.server.rpg.entify.User;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.rpg.system.gm.command.Gm;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright(©) 2017 by xiaomo.
 */
@Slf4j
public class GmManager {

    private static final GmManager ourInstance = new GmManager();

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

        log.warn("玩家:{} 执行gm命令: {}", session.getUser().getLoginName(), command);
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
