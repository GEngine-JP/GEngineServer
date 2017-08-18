package info.xiaomo.server.back;

import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.HandlerPool;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.server.util.MsgExeTimeUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackMessageRouter implements NetworkConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(BackMessageRouter.class);
    private HandlerPool pool;

    public BackMessageRouter(HandlerPool pool) {
        this.pool = pool;
    }

    @Override
    public void consume(AbstractMessage message, Channel channel) {
        int id = message.getId();
        AbstractHandler handler = pool.getHandler(id);

        if (handler == null) {
            return;
        }

        // 方便设断点查看收到客户端的包
        if (MsgExeTimeUtil.isOpen()) {
            handler.setFilter(MsgExeTimeUtil.getFiler());
        }
        handler.setMessage(message);
        handler.doAction();
    }

    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {
        LOGGER.error("接受到断开连接：" + channel);
    }

    @Override
    public void exceptionOccurred(Channel channel, Throwable error) {
        LOGGER.error("消息发生错误Connection:" + channel.toString(), error);
    }
}
