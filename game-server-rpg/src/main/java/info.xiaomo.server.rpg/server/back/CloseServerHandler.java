package info.xiaomo.server.rpg.server.back;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.shared.protocol.gm.CloseServerRequest;

public class CloseServerHandler extends AbstractHandler<CloseServerRequest> {
    @Override
    public void doAction() {
        System.out.println(message.getResMsg());
        Session session = (Session) this.session;
        new GameCloseThread((short) 1, 1, session).start();
    }
}
