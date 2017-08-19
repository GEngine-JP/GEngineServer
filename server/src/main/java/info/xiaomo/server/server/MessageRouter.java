package info.xiaomo.server.server;

import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.base.common.AttributeUtil;
import info.xiaomo.gameCore.persist.jdbc.ConnectionPool;
import info.xiaomo.gameCore.persist.jdbc.DruidConnectionPool;
import info.xiaomo.gameCore.persist.jdbc.JdbcTemplate;
import info.xiaomo.gameCore.protocol.HandlerPool;
import info.xiaomo.gameCore.protocol.NetworkConsumer;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.server.http.HttpServer;
import info.xiaomo.server.system.user.UserManager;
import info.xiaomo.server.util.DruidDBPoolManager;
import info.xiaomo.server.util.MsgExeTimeUtil;
import info.xiaomo.server.util.ScheduleUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageRouter implements NetworkConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouter.class);

    private boolean open = false;

    // 全局发包延迟
    private static int sessionDelay = 0;

    // 是否debug模式
    private static boolean debug = false;

    // 调试临时数据
    public static long temp;

    private HandlerPool pool;

    public static int getSessionDelay() {
        return sessionDelay;
    }

    public static void setSessionDelay(int sessionDelay) {
        MessageRouter.sessionDelay = sessionDelay;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        MessageRouter.debug = debug;
    }

    public static long getTemp() {
        return temp;
    }

    public static void setTemp(long temp) {
        MessageRouter.temp = temp;
    }

    public HandlerPool getPool() {
        return pool;
    }

    public MessageRouter(ServerOption option, HandlerPool pool) throws Exception {
        this.pool = pool;
        debug = option.isDebug();

        // 创建数据库模板
        ConnectionPool connectionPool = new DruidConnectionPool(MessageRouter.class.getResource("/").getPath() + option.getGameDbConfigPath());
        JdbcTemplate template = new JdbcTemplate(connectionPool);
        DruidDBPoolManager.add(option.getServerId(), template);


        // 初始化缓存和数据持久化任务
//        CacheManager.getInstance().init();
//        CacheManager.getInstance().registerPersistTask(new UserPersistFactory());
        HttpServer.start(option.getSpringConfigFile());
    }

    @Override
    public void consume(AbstractMessage message, Channel channel) {

        // 处理登录
        // 处理创建角色、删除角色、
        this.doCommand(message, channel);
    }

    public void doCommand(AbstractMessage message, Channel channel) {

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
        LOGGER.info("接收到连接请求：" + channel);
    }

    @Override
    public void disconnected(Channel channel) {
        LOGGER.error("接受到断开连接：" + channel);
        // 玩家掉线
        Long uid = AttributeUtil.get(channel, SessionAttributeKey.UID);
        Session session = SessionManager.getInstance().getSession(uid);
        if (session == null) {
            return;
        }
        UserManager.getInstance().logout(session.getUser());
    }

    @Override
    public void exceptionOccurred(Channel channel, Throwable error) {
        LOGGER.error("消息发生错误Connection:" + channel.toString(), error);
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        this.open = true;
    }

    public void close() {
        this.open = false;
        ScheduleUtil.shutdown(ScheduleUtil.EVENT_DISPATCHER_EXECUTOR);
        ScheduleUtil.shutdown(ScheduleUtil.STAGE_COMMON_DRIVER_EXECUTOR);
    }

    public void closeAllConnection() {
    }

}
