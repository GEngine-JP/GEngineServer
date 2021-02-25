package info.xiaomo.server.rpg.client;

import info.xiaomo.gengine.network.AbstractHandler;
import info.xiaomo.server.shared.protocol.user.ResUserLogin;

/** Copyright(©) 2017 by xiaomo. */
public class TestResLoginHandler extends AbstractHandler<ResUserLogin> {

    @Override
    public void doAction() {
        System.out.println(message.getMsgId());
        System.out.println(message.getUserId());
        System.out.println(message.getLoginName());
        System.out.println(message.getSex());
    }
}
