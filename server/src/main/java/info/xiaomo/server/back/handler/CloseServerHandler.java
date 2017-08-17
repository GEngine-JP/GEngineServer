package info.xiaomo.server.back.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.server.back.BackManager;
import info.xiaomo.server.message.BackProto.ReqCloseServerMessage;
import info.xiaomo.server.server.UserSession;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/8/17 9:41
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class CloseServerHandler extends AbstractHandler<ReqCloseServerMessage> {
    @Override
    public ReqCloseServerMessage decode(byte[] bytes) throws InvalidProtocolBufferException {
        return ReqCloseServerMessage.parseFrom(bytes);
    }

    @Override
    protected void handMessage(ReqCloseServerMessage msg) {
        BackManager.getInstance().closeServer((short) 1, (UserSession) session);
    }
}
