package info.xiaomo.server.back;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import io.netty.channel.Channel;

public class BackMessageRouter implements NetworkConsumer {

    @Override
    public void consume(AbstractMessage message, Channel channel) {
//        message.doAction();
    }

}
