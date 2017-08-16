package info.xiaomo.server.system.user.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.server.message.UserProto.ReqLoginMessage;
import info.xiaomo.server.server.UserSession;
import info.xiaomo.server.system.user.UserManager;

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
 * Date  : 2017/8/15 17:06
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class LoginHandler extends AbstractHandler<ReqLoginMessage> {

    @Override
    public ReqLoginMessage decode(byte[] paramArrayOfByte) throws InvalidProtocolBufferException {
        return ReqLoginMessage.parseFrom(paramArrayOfByte);
    }

    @Override
    protected void handMessage(ReqLoginMessage req) {
        UserManager.getInstance().login((UserSession) session, req);
    }
}
