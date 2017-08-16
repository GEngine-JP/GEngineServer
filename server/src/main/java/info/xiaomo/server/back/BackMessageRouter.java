package info.xiaomo.server.back;

import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.entity.BaseMsg;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import info.xiaomo.server.server.UserSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackMessageRouter implements MessageExecutor {

    private static Logger LOGGER = LoggerFactory.getLogger(BackMessageRouter.class);


    @Override
    public void doCommand(Channel channel, AbstractHandler handler) throws Exception {

    }

    @Override
    public void connected(ChannelHandlerContext ctx) {

    }

    @Override
    public void disconnected(ChannelHandlerContext ctx) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable paramThrowable) {

    }
}
