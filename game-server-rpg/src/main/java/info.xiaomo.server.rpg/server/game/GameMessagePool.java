package info.xiaomo.server.rpg.server.game;

import info.xiaomo.gengine.network.pool.MessagePool;
import info.xiaomo.server.rpg.system.user.handler.LoginHandler;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.user.ReqUserLogin;
import info.xiaomo.server.shared.protocol.user.ResUserLogin;

/** @author xiaomo 所有消息和handler的消息池 */
public class GameMessagePool extends MessagePool {

    /** 游戏启动的时候注册消息 */
    public GameMessagePool() {
        registerLogin();
    }

    private void registerLogin() {
        register(MsgId.LoginRequest, ReqUserLogin.getDefaultInstance(), LoginHandler.class);
        register(MsgId.LoginResponse, ResUserLogin.getDefaultInstance());
    }
}
