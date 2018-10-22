package info.xiaomo.server.server;

import info.xiaomo.core.network.pool.MessageAndHandlerPool;
import info.xiaomo.server.protocol.UserProto.LoginRequest;
import info.xiaomo.server.protocol.UserProto.LoginResponse;
import info.xiaomo.server.system.user.handler.LoginHandler;

/**
 * @author xiaomo
 * 所有消息和handler的消息池
 */
public class GameMessageAndHandlerPool extends MessageAndHandlerPool {


    /**
     * 游戏启动的时候注册消息
     */
    public GameMessageAndHandlerPool() {
        registerLogin();
    }


    private void registerLogin() {
        register(101101, LoginRequest.getDefaultInstance(), LoginHandler.class);
        register(101102, LoginResponse.getDefaultInstance());
    }


}
