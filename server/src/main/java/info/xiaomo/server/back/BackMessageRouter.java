package info.xiaomo.server.back;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.core.network.INetworkConsumer;
import io.netty.channel.Channel;

public class BackMessageRouter implements INetworkConsumer {

    @Override
    public void consume(AbstractMessage message, Channel channel) {
//        message.doAction();
    }

}
