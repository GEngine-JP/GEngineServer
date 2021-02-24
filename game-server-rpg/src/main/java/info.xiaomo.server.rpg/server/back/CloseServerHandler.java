package info.xiaomo.server.rpg.server.back;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.shared.protocol.gm.ReqGMCloseServer;

public class CloseServerHandler extends AbstractHandler<ReqGMCloseServer> {

    @Override
    public void doAction() {
        System.out.println(message);
        Session session = (Session) this.session;
        new GameCloseThread((short) 1, 1, session).start();
    }
}
