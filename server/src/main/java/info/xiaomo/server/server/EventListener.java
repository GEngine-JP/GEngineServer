package info.xiaomo.server.server;

import info.xiaomo.gameCore.base.common.AttributeUtil;
import info.xiaomo.gameCore.protocol.NetworkEventListener;
import info.xiaomo.server.command.LogoutCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网络事件监听器
 *
 * @author zhangli
 * 2017年6月6日 下午10:00:3
 */
public class EventListener implements NetworkEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onConnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        UserSession session = AttributeUtil.get(channel, SessionKey.SESSION);
        if (session == null) {
            session = new UserSession();
            session.setChannel(channel);
            AttributeUtil.set(channel, SessionKey.SESSION, session);
            LOGGER.debug("接收到新的连接：" + channel.toString());
        } else {
            LOGGER.error("新连接建立时已存在Session，注意排查原因" + channel.toString());
        }
    }

    @Override
    public void onDisconnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        UserSession session = AttributeUtil.get(channel, SessionKey.SESSION);
        closeSession(session);
    }

    @Override
    public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {

    }

    public static void closeSession(UserSession session) {
        if (session == null || session.getUser() == null) {
            //下线
            LOGGER.error("玩家断开连接[没有找到用户信息]");
            return;
        }
        MessageProcessor processor = Context.getGameServer().getRouter().getProcessor(1);
        processor.process(new LogoutCommand(session));
    }


}
