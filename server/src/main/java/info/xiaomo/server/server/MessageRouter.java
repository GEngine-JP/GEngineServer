package info.xiaomo.server.server;

import info.xiaomo.gameCore.base.common.AttributeUtil;
import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.handler.MessageExecutor;
import info.xiaomo.server.command.LogoutCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
 * Date  : 2017/7/11 16:54
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class MessageRouter implements MessageExecutor {
    private static Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private Map<Integer, MessageProcessor> processors = new HashMap<>();

    public void registerProcessor(int queueId, MessageProcessor consumer) {
        processors.put(queueId, consumer);
    }


    public MessageProcessor getProcessor(int queueId) {
        return processors.get(queueId);
    }

    @Override
    public void doCommand(Channel channel, AbstractHandler handler) throws Exception {
        //将消息分发到指定的队列中，该队列有可能在同一个进程，也有可能不在同一个进程
        MessageProcessor messageProcessor = processors.get(1);
        messageProcessor.process(handler);
    }

    @Override
    public void connected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        UserSession session = AttributeUtil.get(channel, SessionKey.SESSION);
        if (session == null) {
            session = new UserSession();
            session.setChannel(channel);
            AttributeUtil.set(channel, SessionKey.SESSION, session);
            LOGGER.error("接收到新的连接：" + channel.toString());
        } else {
            LOGGER.error("新连接建立时已存在Session，注意排查原因" + channel.toString());
        }
    }

    @Override
    public void disconnected(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        UserSession session = AttributeUtil.get(channel, SessionKey.SESSION);
        closeSession(session);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable paramThrowable) {

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
