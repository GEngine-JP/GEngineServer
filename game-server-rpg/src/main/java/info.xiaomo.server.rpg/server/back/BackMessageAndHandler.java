package info.xiaomo.server.rpg.server.back;


import info.xiaomo.gengine.network.pool.MessageAndHandlerPool;
import info.xiaomo.server.shared.protocol.gm.CloseServerRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;

/**
 * Copyright(©) 2017 by xiaomo.
 */
public class BackMessageAndHandler extends MessageAndHandlerPool {


    /**
     * 游戏启动的时候注册消息
     */
    public BackMessageAndHandler() {
        register(MsgId.CloseServer.getNumber(), CloseServerRequest.getDefaultInstance(), CloseServerHandler.class);
    }

}
