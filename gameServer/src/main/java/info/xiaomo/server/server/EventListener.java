package info.xiaomo.server.server;


import info.xiaomo.core.net.NetworkEventListener;
import info.xiaomo.core.util.AttributeUtil;
import info.xiaomo.server.command.LogoutCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 16:01
 * desc  : 事件监听器
 * Copyright(©) 2017 by xiaomo.
 */
public class EventListener implements NetworkEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onConnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Session session = AttributeUtil.get(channel, SessionKey.SESSION);
        if(session == null){
            session = new Session();
            session.setChannel(channel);
            AttributeUtil.set(channel, SessionKey.SESSION, session);
            LOGGER.error("接收到新的连接：" + channel.toString());
        } else {
            LOGGER.error("新连接建立时已存在Session，注意排查原因" + channel.toString());
        }
    }

    @Override
    public void onDisconnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        Session session = AttributeUtil.get(channel, SessionKey.SESSION);
        closeSession(session);
    }

    @Override
    public void onExceptionOccur(ChannelHandlerContext ctx, Throwable cause) {

    }

    public static void closeSession(Session session){
        if(session == null || session.getUser() == null) {
            //下线
            LOGGER.error("玩家断开连接[没有找到用户信息]");
            return;
        }
        MessageProcessor processor = Context.getGameServer().getRouter().getProcessor(1);
        processor.process(new LogoutCommand(session));
    }
}
