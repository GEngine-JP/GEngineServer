package info.xiaomo.server.back;

import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackMessageRouter implements NetworkConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(BackMessageRouter.class);


    @Override
    public void consume(AbstractMessage message, Channel channel) {

    }

    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {

    }

    @Override
    public void exceptionOccurred(Channel channel, Throwable error) {

    }
}
