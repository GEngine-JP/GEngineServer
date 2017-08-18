package info.xiaomo.server.back;

import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.base.common.AttributeUtil;
import info.xiaomo.gameCore.protocol.HandlerPool;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.server.entify.Player;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.server.SessionAttributeKey;
import info.xiaomo.server.server.SessionManager;
import info.xiaomo.server.util.MsgExeTimeUtil;
import info.xiaomo.server.util.ScheduleUtil;
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
        Long uid = AttributeUtil.get(channel, SessionAttributeKey.UID);
        Session session = null;
        int id = message.getId();
        if (uid != null) {
            session = SessionManager.getInstance().getSession(uid);
        }
        if (session == null) {
            //未登录
            return;
        }

        //System.out.println(message.getClass());

        // 处理登录
        // 处理创建角色

        AbstractHandler handler = pool.getHandler(id);

        if (handler == null) {
            return;
        }

        // 方便设断点查看收到客户端的包
        if (MsgExeTimeUtil.isOpen()) {
            handler.setFilter(MsgExeTimeUtil.getFiler());
        }

        handler.setParam(session);
        handler.setMessage(message);
        Player player = session.getPlayer();

        if (id == 100101) {
            ScheduleUtil.HEARTBEAT_EXECUTOR.addTask(0L, handler);
            return;
        }

        if (id / 1000 == 101 || id == 300103) {
            ScheduleUtil.LOGIN_EXECUTOR.addTask(0L, handler);
        } else {
            LOGGER.error("player == null, user = {}, msgId = {}", session.getUser(), id);
        }
    }

    @Override
    public void connected(Channel channel) {

    }

    @Override
    public void disconnected(Channel channel) {
        LOGGER.error("接受到断开连接：" + channel);
        // 玩家掉线
        Long uid = AttributeUtil.get(channel, SessionAttributeKey.UID);
        if (uid == null) {
            return;
        }
        Session session = SessionManager.getInstance().getSession(uid);
        if (session != null) {
            if (session.getPlayer() != null) {
                LOGGER.error("玩家:{}断网下线!, {}, {}", session.getPlayer().getName(), session.getChannel(),
                        Thread.currentThread().getId());
            }
            SessionManager.getInstance().unregister(session);
        }
    }

    @Override
    public void exceptionOccurred(Channel channel, Throwable error) {
        LOGGER.error("消息发生错误Connection:" + channel.toString(), error);
    }
}
