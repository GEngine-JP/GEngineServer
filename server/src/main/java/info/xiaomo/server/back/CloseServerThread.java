package info.xiaomo.server.back;

import info.xiaomo.server.http.HttpServer;
import info.xiaomo.server.server.GameContext;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.util.ScheduleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2016/8/3 15:00.
 *
 * @author 周锟
 */
public class CloseServerThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseServerThread.class);

    private Session session;

    private int source;

    public CloseServerThread(Session session, int source) {
        this.session = session;
        this.source = source;
    }

    private void sendMsg(int code, String msg) {
        LOGGER.info("source={}, code={}, msg={}", source, code, msg);

        if (source != 1) {
            return;
        }
        if (session == null) {
            return;
        }
//		ResCloseServerMessage res = new ResCloseServerMessage();
//		res.setCloseServerResponse(CloseServerResponse.newBuilder()
//				.setCode(code)
//				.setMsg(msg)
//				.build());
        try {
//            session.getChannel().writeAndFlush(res).sync();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.error("closeServer", e);
        }
    }

    private void closeConnections() {
        ScheduleUtil.LOGIN_EXECUTOR.shutdown();
        GameContext.getInstance().closeAllConnection();
        sendMsg(1, "关闭所有连接");
    }

    private void closeNetwork() {
        sendMsg(2, "关闭网络连接");
        GameContext.getInstance().getGameServer().closeNetWork();
        HttpServer.stop();
    }

    private void closeWorld() {
        GameContext.getInstance().getGameServer().closeNetWork();
        sendMsg(3, "关闭世界");
    }

//	private void saveData() {
//		OffLineRoleManager.getInstance().forceBackToGame(0);
//		CacheManager.getInstance().store();
//		RankManager.getInstance().onCloseServer();
//		ScheduleUtil.shutdown(ScheduleUtil.getExecutor());
//		sendMsg(4, "保存数据");
//	}


    private void closeBackServer() {
        sendMsg(5, "关闭GM端口");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
        GameContext.getInstance().getGameServer().closeBackServer();
    }

    @Override
    public void run() {
        closeConnections();
        closeNetwork();
        closeWorld();
//        saveData();
        closeBackServer();

        LOGGER.info("code={}, msg={}", 6, "工作完成，准备退出");
        if (source != 2) {
            LOGGER.info("code={}, msg={}", 7, "System.exit(0)");
            System.exit(0);
        }
    }
}
