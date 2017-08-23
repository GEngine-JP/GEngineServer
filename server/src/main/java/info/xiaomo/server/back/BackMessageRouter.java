package info.xiaomo.server.back;

import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import io.netty.channel.Channel;

public class BackMessageRouter implements NetworkConsumer {

    @Override
    public void consume(Message message, Channel channel) {
        message.doAction();
    }

}
