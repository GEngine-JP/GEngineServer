package info.xiaomo.server.back;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gengine.network.netty.INetworkConsumer;
import io.netty.channel.Channel;

/**
 * @author qq
 */
public class BackMessageRouter implements INetworkConsumer {

    @Override
    public void consume(AbstractMessage message, Channel channel) {
//        message.doAction();
    }

}
