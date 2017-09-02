package info.xiaomo.server.system.user.handler;

import info.xiaomo.gameCore.network.AbstractHandler;
import info.xiaomo.server.protocol.UserProto.LoginRequest;
import info.xiaomo.server.server.Session;
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
 * Date  : 2017/8/28 16:24
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class LoginHandler extends AbstractHandler<LoginRequest> {

    @Override
    public void doAction() {
        Session session = (Session) this.session;
        UserManager.getInstance().login(session, message.getLoginName());
    }


}
