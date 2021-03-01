package info.xiaomo.server.rpg.server.back;

import com.google.protobuf.Message;
import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.gengine.network.IMessagePool;
import info.xiaomo.gengine.network.pool.MessageRouter;
import info.xiaomo.server.rpg.server.game.Session;
import io.netty.channel.Channel;

/** @author qq */
public class BackMessageRouter extends MessageRouter {

    public BackMessageRouter(IMessagePool msgPool) {
        super(msgPool);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void consume(Message msgPack, Channel channel) {
        int msgId = msgPool.getMessageId(msgPack);
        AbstractHandler handler = msgPool.getHandler(msgId);
        handler.setMessage(msgPack);
        Session session = new Session(channel);
        handler.setSession(session);
        handler.doAction();
    }
}
