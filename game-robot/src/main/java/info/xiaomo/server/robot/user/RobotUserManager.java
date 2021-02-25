package info.xiaomo.server.robot.user;

import info.xiaomo.server.rpg.server.game.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobotUserManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobotUserManager.class);
    static RobotUserManager INSTANCE = new RobotUserManager();

    public static RobotUserManager getInstance() {
        return INSTANCE;
    }

    public void createRole(Session session, long uid, String name) {
        //
        //		ReqCreateRoleMessage req = new ReqCreateRoleMessage();
        //		req.setCareer(1);
        //		req.setSex(1);
        //		req.setRoleName(name);
        //
        //		SessionManager.getInstance().addSession(uid, session);
        //		MessageUtil.sendMsg(req, uid);

    }

    public void enterGame(int mapId, int line) {
        LOGGER.info("准备进入游戏:map->" + mapId + ",line->" + line);
    }
}
