package info.xiamo.server.robot.handle;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.gengine.network.INetworkConsumer;
import info.xiaomo.gengine.network.MsgPack;
import info.xiaomo.gengine.network.SessionKey;
import info.xiaomo.gengine.utils.AttributeUtil;
import info.xiaomo.server.rpg.server.game.Session;
import io.netty.channel.Channel;

public class RobotConsumer implements INetworkConsumer {

    private final RobotMessagePool msgPool;

    public RobotConsumer(RobotMessagePool msgPool) {
        this.msgPool = msgPool;
    }

    @Override
    public void consume(MsgPack msg, Channel channel) {
        Session session = (Session) AttributeUtil.get(channel, SessionKey.SESSION);

        if (session == null) {
            return;
        }
        AbstractHandler handler = msgPool.getHandler(msg.getMsgId());
        handler.doAction();
    }
}
