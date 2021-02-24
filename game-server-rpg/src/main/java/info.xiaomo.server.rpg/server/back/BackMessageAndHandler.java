package info.xiaomo.server.rpg.server.back;


import info.xiaomo.gengine.network.pool.MessageAndHandlerPool;
import info.xiaomo.server.shared.protocol.gm.ReqGMCloseServer;
import info.xiaomo.server.shared.protocol.msg.GMMsgId;

/**
 * Copyright(©) 2017 by xiaomo.
 */
public class BackMessageAndHandler extends MessageAndHandlerPool {


    /**
     * 游戏启动的时候注册消息
     */
    public BackMessageAndHandler() {
        register(GMMsgId.CloseServerRequest.getNumber(), ReqGMCloseServer.getDefaultInstance(), CloseServerHandler.class);
    }

}
