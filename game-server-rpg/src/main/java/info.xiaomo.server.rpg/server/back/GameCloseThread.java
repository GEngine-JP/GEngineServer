package info.xiaomo.server.rpg.server.back;

import java.util.concurrent.TimeUnit;
import info.xiaomo.gengine.concurrent.QueueExecutor;
import info.xiaomo.gengine.event.EventUtil;
import info.xiaomo.gengine.utils.ExecutorUtil;
import info.xiaomo.gengine.utils.TimeUtil;
import info.xiaomo.server.rpg.db.DataCenter;
import info.xiaomo.server.rpg.event.EventType;
import info.xiaomo.server.rpg.server.game.GameContext;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.rpg.server.game.SessionManager;
import info.xiaomo.server.rpg.util.MessageUtil;
import info.xiaomo.server.shared.protocol.gm.ResGMCloseServer;
import info.xiaomo.server.shared.protocol.msg.GMMsgId;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务器关闭线程
 *
 * @author 张力
 * @date 2015-6-15 下午6:57:51
 */
public class GameCloseThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameCloseThread.class);
    private short sequence;
    /** 关服来源1：命令行，2：后台，3：GM命令，4：JVM钩子 */
    private final int source;

    private final Session session;

    public GameCloseThread(short sequence, int source, Session session) {
        super();
        this.sequence = sequence;
        this.source = source;
        this.session = session;
        this.setDaemon(false);
    }

    private void sendMsg(int code, String text) {
        if (session == null) {
            return;
        }
        ResGMCloseServer resGMCloseServer =
                ResGMCloseServer.newBuilder()
                        .setMsgId(GMMsgId.CloseServerResponse)
                        .setResMsg(text + code)
                        .build();
        MessageUtil.sendMsg(session, resGMCloseServer);
    }

    private void closeAllConnection() {

        if (source == 1) {
            sendMsg(1, "断开所有连接....");
        }
        LOGGER.info("断开所有连接....");

        Session[] sessions = SessionManager.getInstance().sessionArray();
        for (Session session : sessions) {

            session.close();
            ChannelFuture future = session.getChannel().close();
            try {
                future.get(1000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOGGER.error(session + ",停服关闭连接失败");
            }
            //            MessageRouter.closeSession(session);
        }
    }

    private void fireCloseEvent() {
        try {
            EventUtil.fireEvent(EventType.SERVER_SHUTDOWN);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    private void closeLoginThread() {
        if (source == 1) {
            sendMsg(1, "关闭登录线程....");
        }
        LOGGER.info("关闭登录线程....");

        // 等待登录线程中的命令执行完毕
        int totalTime = 60000;
        while (totalTime > 0 && !ExecutorUtil.LOGIN_EXECUTOR.getQueue().isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error("", e);
            }
            totalTime -= 100;
        }
        // 关闭登录线程
        try {
            ExecutorUtil.LOGIN_EXECUTOR.shutdown();
            int total = 10;
            while (total > 0
                    && !ExecutorUtil.LOGIN_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭登录线程...");
                total--;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    private void closeEventDispatchThread() {
        if (source == 1) {
            sendMsg(1, "关闭场景事件派发线程....");
        }
        LOGGER.info("关闭场景事件派发线程....");
        // 关闭场景事件派发线程
        try {
            int total = 10;
            ExecutorUtil.EVENT_DISPATCHER_EXECUTOR.shutdown();
            while (total > 0
                    && !ExecutorUtil.EVENT_DISPATCHER_EXECUTOR.awaitTermination(
                            1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭场景事件派发线程...");
                total--;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    public void closeStageDriverThread() {
        if (source == 1) {
            sendMsg(1, "等待所有场景中的命令执行完毕....");
        }
        LOGGER.info("等待所有场景中的命令执行完毕....");
        // 等待所有场景中的命令执行完毕
        int totalTime = 5000;

        //        GameMap[] maps = MapManager.getInstance().getMaps().values().toArray(new
        // GameMap[0]);
        //        for (GameMap map : maps) {
        //            QueueDriver driver = map.getDriver();
        //            while (totalTime > 0 && driver.getQueueSize() > 0) {
        //                LOGGER.info("{}的队列剩余：{}", map.getName(), driver.getQueueSize());
        //                try {
        //                    Thread.sleep(100);
        //                } catch (InterruptedException e) {
        //                    LOGGER.error("", e);
        //                }
        //                totalTime -= 100;
        //            }
        //        }

        if (source == 1) {
            sendMsg(1, "关闭场景公共驱动线程....");
        }
        LOGGER.info("关闭场景公共驱动线程....");
        // 关闭场景公共驱动线程
        try {
            int total = 10;
            ExecutorUtil.COMMON_DRIVER_EXECUTOR.shutdown();
            while (total > 0
                    && !ExecutorUtil.COMMON_DRIVER_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭场景公共驱动线程...");
                total--;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }

        // 关闭独立场景驱动线程
        for (QueueExecutor executor : ExecutorUtil.SPECIAL_DRIVER_EXECUTOR_MAP.values()) {
            if (source == 1) {
                sendMsg(1, "关闭" + executor.getName() + "....");
            }
            LOGGER.info("关闭" + executor.getName() + "....");
            try {
                int total = 5;
                executor.shutdown();
                while (total > 0 && !executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    LOGGER.info("正在关闭" + executor.getName() + "....");
                    total--;
                }
            } catch (InterruptedException e) {
                LOGGER.error("", e);
            }
        }
    }

    public void closeLogicThread() {
        if (source == 1) {
            sendMsg(1, "关闭业务逻辑线程....");
        }
        LOGGER.info("关闭业务逻辑线程....");
        // 关闭业务逻辑线程
        try {
            ExecutorUtil.COMMON_LOGIC_EXECUTOR.shutdown();
            int total = 5;
            // 允许两秒的时间执行当前已经执行的任务
            while (total > 0
                    && !ExecutorUtil.COMMON_LOGIC_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭业务逻辑线程....");
                total -= 1;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    private void saveAllData() {
        // 保存玩家数据
        if (source == 1) {
            sendMsg(2, "关闭数据保存线程(保存服务器缓存数据)....");
        }
        LOGGER.info("关闭数据保存线程(保存服务器缓存数据)....");
        DataCenter.store();

        // 持久化排行榜
        if (source == 1) {
            sendMsg(3, "保存排行榜缓存数据....");
        }
        LOGGER.info("保存排行榜缓存数据....");
        // RankManager.getInstance().rankStopServer();

        // 持久化邮件系统
        if (source == 1) {
            sendMsg(4, "保存邮件数据....");
        }
        LOGGER.info("保存邮件数据....");
        // MailManager.getInstance().onServerClose();

        // 保存延迟写入数据
        if (source == 1) {
            sendMsg(4, "关闭延迟入库线程(保存未入库的数据)....");
        }
        /*LOGGER.error("关闭延迟入库线程(保存未入库的数据)....");
              try {
        	DelayQueryThread.stop();
        } catch (InterruptedException e) {
        	LOGGER.error("", e);
        }*/
    }

    private void closeNetWork() {
        // 关闭Grizzly
        if (source == 1) {
            sendMsg(5, "关闭Netty网络监听....");
        }
        LOGGER.info("关闭Netty网络监听....");
        GameContext.getGameServer().stop();
    }

    @Override
    public void run() {

        // 关服逻辑执行标记
        GameContext.setServerCloseLogicExecuted(true);

        GameContext.setClosed(true);
        long serverCloseLogicExecutedStart = TimeUtil.getNowOfMills();
        fireCloseEvent(); // 发送停服事件 ok
        long serverCloseLogicExecutedStartEnd = TimeUtil.getNowOfMills();
        LOGGER.error(
                "停服事件耗时:" + (serverCloseLogicExecutedStartEnd - serverCloseLogicExecutedStart));

        long closeAllConnectionStart = TimeUtil.getNowOfMills();
        closeAllConnection(); // 断开所有连接
        long closeAllConnectionEnd = TimeUtil.getNowOfMills();
        LOGGER.error(
                "断开所有连接------------------------:"
                        + (closeAllConnectionEnd - closeAllConnectionStart));

        long closeLoginThreadStart = TimeUtil.getNowOfMills();
        closeLoginThread(); // 关闭登录线程 ok
        long closeLoginThreadEnd = TimeUtil.getNowOfMills();
        LOGGER.error("关闭登录线程-------------------:" + (closeLoginThreadEnd - closeLoginThreadStart));

        long closeLogicThreadStart = TimeUtil.getNowOfMills();
        closeLogicThread(); // closeLogicThread ok
        long closeLogicThreadEnd = TimeUtil.getNowOfMills();
        LOGGER.error("关闭业务线程--------------:" + (closeLogicThreadEnd - closeLogicThreadStart));

        long closeEventDispatchThreadStart = TimeUtil.getNowOfMills();
        closeEventDispatchThread(); // 关闭事件派发起线程 ok
        long closeEventDispatchThreadEnd = TimeUtil.getNowOfMills();
        LOGGER.error(
                "关闭事件派发起线程-------------------:"
                        + (closeEventDispatchThreadEnd - closeEventDispatchThreadStart));

        long closeStageDriverThreadStart = TimeUtil.getNowOfMills();
        closeStageDriverThread(); // 关闭场景驱动线程 ok
        long closeStageDriverThreadEnd = TimeUtil.getNowOfMills();
        LOGGER.error(
                "关闭场景驱动线程-------------:"
                        + (closeStageDriverThreadEnd - closeStageDriverThreadStart));

        long saveAllDataStart = TimeUtil.getNowOfMills();
        saveAllData(); // 保存数据 ok
        long saveAllDataEnd = TimeUtil.getNowOfMills();
        LOGGER.error("saveAllData--------------------:" + (saveAllDataEnd - saveAllDataStart));

        long closeNetWorkStart = TimeUtil.getNowOfMills();
        closeNetWork(); // 关闭网络 ok
        long closeNetWorkEnd = TimeUtil.getNowOfMills();
        LOGGER.error("关闭网络---------------:" + (closeNetWorkEnd - closeNetWorkStart));

        if (source == 1) {
            sendMsg(1, "关服逻辑处理完毕...");
        }
        LOGGER.info("关服逻辑处理完毕...");

        LOGGER.info("服务器已关闭...");

        if (session != null) {
            try {
                sendMsg(-1, "服务器己关闭");
            } catch (Exception e) {
                LOGGER.error("发送服务器关闭消息出错.", e);
            }
        }

        LOGGER.info("退出程序...");
        int ret = 4;
        if (source != ret) {
            System.exit(0);
        }
    }
}
