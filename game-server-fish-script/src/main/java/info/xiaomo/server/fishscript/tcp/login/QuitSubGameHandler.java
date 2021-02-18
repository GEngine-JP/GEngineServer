package info.xiaomo.server.fishscript.tcp.login;

import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.fish.manager.RoleManager;
import info.xiaomo.server.fish.manager.RoomManager;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.protocol.hall.login.QuitSubGameRequest;
import info.xiaomo.server.shared.protocol.hall.login.QuitSubGameResponse;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 退出子游戏 TODO 数据持久化等处理
 * <p>
 * <p>
 * 2017年7月26日 下午5:34:06
 */
@HandlerEntity(mid = MsgId.QuitSubGameReq_VALUE, msg = QuitSubGameRequest.class)
public class QuitSubGameHandler extends TcpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuitSubGameHandler.class);

    @Override
    public void run() {
        QuitSubGameRequest req = getMsg();
        LOGGER.info("{}退出捕鱼", getRid());
        UserRole role = RoleManager.getInstance().getRole(getRid());
        if (role == null) {
            return;
        }
        // 退出房间
        if (role.getRoomId() > 0) {
            RoomManager.getInstance().quitRoom(role, role.getRoomId());
        }

        RoleManager.getInstance().quit(role, GlobalReason.UserQuit);

        QuitSubGameResponse.Builder builder = QuitSubGameResponse.newBuilder();
        builder.setResult(0);
        sendIdMsg(builder.build());
    }

}
