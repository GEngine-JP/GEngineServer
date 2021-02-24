package info.xiaomo.server.rpg.system.user.handler;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.rpg.system.user.UserManager;
import info.xiaomo.server.shared.protocol.user.ReqUserLogin;

/** Copyright(©) 2017 by xiaomo. */
public class LoginHandler extends AbstractHandler<ReqUserLogin> {

    @Override
    public void doAction() {
        Session session = (Session) this.session;
        UserManager.getInstance().login(session, message.getLoginName());
    }
}
