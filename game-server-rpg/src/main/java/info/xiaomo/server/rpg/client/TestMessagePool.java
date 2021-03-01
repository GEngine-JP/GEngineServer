package info.xiaomo.server.rpg.client;

import info.xiaomo.gengine.network.pool.MessagePool;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import info.xiaomo.server.shared.protocol.user.ReqUserLogin;
import info.xiaomo.server.shared.protocol.user.ResUserLogin;

/** @author xiaomo 所有消息和handler的消息池 */
public class TestMessagePool extends MessagePool {

    /** 游戏启动的时候注册消息 */
    public TestMessagePool() {
        registerLogin();
    }

    private void registerLogin() {
        register(MsgId.LoginRequest, ReqUserLogin.getDefaultInstance());
        register(MsgId.LoginResponse, ResUserLogin.getDefaultInstance(), TestLoginHandler.class);
    }
}
