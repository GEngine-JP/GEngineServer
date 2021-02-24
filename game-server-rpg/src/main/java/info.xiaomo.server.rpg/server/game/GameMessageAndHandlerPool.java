package info.xiaomo.server.rpg.server.game;

import info.xiaomo.gengine.network.pool.MessageAndHandlerPool;
import info.xiaomo.server.rpg.system.user.handler.LoginHandler;
import info.xiaomo.server.shared.protocol.user.ReqUserLogin;
import info.xiaomo.server.shared.protocol.user.ResUserLogin;

/** @author xiaomo 所有消息和handler的消息池 */
public class GameMessageAndHandlerPool extends MessageAndHandlerPool {

    /** 游戏启动的时候注册消息 */
    public GameMessageAndHandlerPool() {
        registerLogin();
    }

    private void registerLogin() {
        register(101101, ReqUserLogin.getDefaultInstance(), LoginHandler.class);
        register(101102, ResUserLogin.getDefaultInstance());
    }
}
