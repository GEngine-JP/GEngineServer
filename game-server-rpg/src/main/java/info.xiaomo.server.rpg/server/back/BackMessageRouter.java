package info.xiaomo.server.rpg.server.back;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.gengine.network.IMessageAndHandler;
import info.xiaomo.gengine.network.MsgPack;
import info.xiaomo.gengine.network.pool.MessageRouter;
import info.xiaomo.server.rpg.server.game.Session;
import io.netty.channel.Channel;

/** @author qq */
public class BackMessageRouter extends MessageRouter {

    public BackMessageRouter(IMessageAndHandler msgPool) {
        super(msgPool);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void consume(MsgPack msgPack, Channel channel) {
        AbstractHandler handler = msgPool.getHandler(msgPack.getMsgId());
        handler.setMessage(msgPack.getMsg());
        Session session = new Session(channel);
        handler.setSession(session);
        handler.doAction();
    }
}
