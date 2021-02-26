package info.xiaomo.server.rpg.server.back;

import info.xiaomo.gengine.network.pool.MessagePool;
import info.xiaomo.server.shared.protocol.gm.ReqGMCloseServer;
import info.xiaomo.server.shared.protocol.msg.MsgId;

/** Copyright(©) 2017 by xiaomo. */
public class BackMessagePool extends MessagePool {

    /** 游戏启动的时候注册消息 */
    public BackMessagePool() {
        register(
                MsgId.CloseServerRequest.getNumber(),
                ReqGMCloseServer.getDefaultInstance(),
                CloseServerHandler.class);
    }
}
