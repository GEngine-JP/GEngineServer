package info.xiaomo.server.rpg.server.game;

import info.xiaomo.gengine.network.pool.MessagePool;
import info.xiaomo.server.rpg.client.TestResLoginHandler;
import info.xiaomo.server.rpg.system.user.handler.LoginHandler;
import info.xiaomo.server.shared.protocol.msg.UserMsgId;
import info.xiaomo.server.shared.protocol.user.ReqUserLogin;
import info.xiaomo.server.shared.protocol.user.ResUserLogin;

/** @author xiaomo 所有消息和handler的消息池 */
public class GameMessagePool extends MessagePool {

    /** 游戏启动的时候注册消息 */
    public GameMessagePool() {
        registerLogin();
    }

    private void registerLogin() {
        register(
                UserMsgId.LoginRequest_VALUE,
                ReqUserLogin.getDefaultInstance(),
                LoginHandler.class);
        register(
                UserMsgId.LoginResponse_VALUE,
                ResUserLogin.getDefaultInstance(),
                TestResLoginHandler.class);
    }
}
