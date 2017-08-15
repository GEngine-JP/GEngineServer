package info.xiaomo.server.back;

import info.xiaomo.gameCore.base.concurrent.CommandQueueExecutor;
import info.xiaomo.server.db.DataCenter;
import info.xiaomo.server.event.EventType;
import info.xiaomo.server.event.EventUtil;
import info.xiaomo.server.server.Context;
import info.xiaomo.server.server.MessageRouter;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.server.SessionManager;
import info.xiaomo.server.util.ExecutorUtil;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static info.xiaomo.server.back.GameCloseThread.SourceType.COMMAND_LINE;
import static info.xiaomo.server.back.GameCloseThread.SourceType.JVM_HOKE;

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
 * Date  : 2017/7/13 14:36
 * desc  : 关服线程
 * Copyright(©) 2017 by xiaomo.
 */
public class GameCloseThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameCloseThread.class);
    private short sequence;
    /**
     * 关服来源1：命令行，2：后台，3：GM命令，4：JVM钩子
     *
     * @see SourceType
     */
    private int source;

    private Session session;

    public GameCloseThread(short sequence, int source, Session session) {
        super();
        this.sequence = sequence;
        this.source = source;
        this.session = session;
    }

    private void sendMsg(int code, String text) {
        if (session == null) {
            return;
        }
//        ResCloseServerMessage msg = new ResCloseServerMessage();
//        msg.setSequence(sequence);
//        msg.setCode(code);
//        msg.setInfo(text);
//        session.sendMessage(msg);
    }

    /**
     * 关闭所有链接
     */
    private void closeAllConnection() {

        if (source == COMMAND_LINE) {
            sendMsg(1, "断开所有连接....");
        }
        LOGGER.info("关闭数据保存线程(保存服务器缓存数据)....");
        DataCenter.store();


        Session[] sessions = SessionManager.getInstance().sessionArray();
        for (Session session : sessions) {

            ChannelFuture close = session.close();
            if (close == null) {
                continue;
            }
            ChannelFuture future = session.getChannel().close();
            try {
                future.get(1000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                LOGGER.error(session + ",停服关闭连接失败");
            }
            MessageRouter.closeSession(session);
        }

    }

    @Override
    public void run() {

        //关服逻辑执行标记
        Context.setServerCloseLogicExecuted(true);

        Context.setClosed(true);

        fireCloseEvent();// 发送停服事件 ok

        closeAllConnection();// 断开所有连接

        closeLoginThread();// 关闭登录线程 ok

        closeLogicThread();// 关闭业务逻辑线程 ok

        closeEventDispatchThread(); // 关闭事件派发起线程 ok

        closeStageDriverThread(); // 关闭场景驱动线程 ok

        saveAllData();// 保存数据 ok

        closeNetWork();// 关闭网络 ok

        if (source == COMMAND_LINE) {
            sendMsg(1, "关服逻辑处理完毕...");
        }
        LOGGER.info("关服逻辑处理完毕...");

        LOGGER.info("服务器已关闭...");

        if (session != null) {
//            ResCloseServerMessage msg = new ResCloseServerMessage();
//            msg.setSequence(sequence);
//            msg.setCode(-1);
//            msg.setInfo("服务器已关闭...");
            try {
//                ChannelFuture fu = session.getChannel().writeAndFlush(msg).sync();
//                fu.get();
            } catch (Exception e) {
                LOGGER.error("发送服务器关闭消息出错.", e);
            }
        }

        LOGGER.info("退出程序...");
        if (source != JVM_HOKE) {
            System.exit(0);
        }
    }

    /**
     * 执行关服事件
     */
    private void fireCloseEvent() {
        try {
            EventUtil.executeEvent(EventType.SERVER_SHUTDOWN, null);
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    private void closeLoginThread() {
        if (source == COMMAND_LINE) {
            sendMsg(COMMAND_LINE, "关闭登录线程....");
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
            while (total > 0 && !ExecutorUtil.LOGIN_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭登录线程...");
                total--;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }

    }


    private void closeLogicThread() {
        if (source == COMMAND_LINE) {
            sendMsg(COMMAND_LINE, "关闭业务逻辑线程....");
        }
        LOGGER.info("关闭业务逻辑线程....");
        // 关闭业务逻辑线程
        try {
            ExecutorUtil.COMMON_LOGIC_EXECUTOR.shutdown();
            int total = 5;
            // 允许两秒的时间执行当前已经执行的任务
            while (total > 0 && !ExecutorUtil.COMMON_LOGIC_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭业务逻辑线程....");
                total -= 1;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }

    private void saveAllData() {
        // 保存玩家数据
        if (source == COMMAND_LINE) {
            sendMsg(2, "关闭数据保存线程(保存服务器缓存数据)....");
        }
        LOGGER.info("关闭数据保存线程(保存服务器缓存数据)....");
        DataCenter.store();

        // 持久化排行榜
        if (source == COMMAND_LINE) {
            sendMsg(3, "保存排行榜缓存数据....");
        }
        LOGGER.info("保存排行榜缓存数据....");
        //RankManager.getInstance().rankStopServer();

        // 持久化邮件系统
        if (source == COMMAND_LINE) {
            sendMsg(4, "保存邮件数据....");
        }
        LOGGER.info("保存邮件数据....");
        //MailManager.getInstance().onServerClose();

        // 保存延迟写入数据
        if (source == COMMAND_LINE) {
            sendMsg(4, "关闭延迟入库线程(保存未入库的数据)....");
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
            while (total > 0 && !ExecutorUtil.EVENT_DISPATCHER_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭场景事件派发线程...");
                total--;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }


    private void closeStageDriverThread() {
        if (source == COMMAND_LINE) {
            sendMsg(1, "等待所有场景中的命令执行完毕....");
        }
        LOGGER.info("等待所有场景中的命令执行完毕....");
        // 等待所有场景中的命令执行完毕
//        int totalTime = 5000;

//        GameMap[] maps = MapManager.getInstance().getMaps().values().toArray(new GameMap[0]);
//        for (GameMap map : maps) {
//            QueueDriver driver = map.getDriver();
//            while (totalTime > 0 && driver.getQueueSize() > 0) {
//                LOGGER.info("{}的队列剩余：{}" ,map.getNickName() ,driver.getQueueSize());
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    LOGGER.error("", e);
//                }
//                totalTime -= 100;
//            }
//        }


        if (source == COMMAND_LINE) {
            sendMsg(1, "关闭场景公共驱动线程....");
        }
        LOGGER.info("关闭场景公共驱动线程....");
        // 关闭场景公共驱动线程
        try {
            int total = 10;
            ExecutorUtil.COMMON_DRIVER_EXECUTOR.shutdown();
            while (total > 0 && !ExecutorUtil.COMMON_DRIVER_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("正在关闭场景公共驱动线程...");
                total--;
            }
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }

        // 关闭独立场景驱动线程
        for (CommandQueueExecutor executor : ExecutorUtil.SPECIAL_DRIVER_EXECUTOR_MAP.values()) {
            if (source == COMMAND_LINE) {
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

    private void closeNetWork() {
        if (source == COMMAND_LINE) {
            sendMsg(5, "通过【命令行】关闭Netty网络监听....");
        }
        LOGGER.info("关闭Netty网络监听....");
        Context.getGameServer().stop();

    }

    public interface SourceType {
        int COMMAND_LINE = 1;
        int BACK_SERVER = 2;
        int GM_COMMAND = 3;
        int JVM_HOKE = 4;
    }
}
