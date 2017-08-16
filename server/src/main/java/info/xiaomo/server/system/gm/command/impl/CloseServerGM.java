package info.xiaomo.server.system.gm.command.impl;

import info.xiaomo.server.back.GameCloseThread;
import info.xiaomo.server.server.UserSession;
import info.xiaomo.server.system.gm.command.GM;

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
 * Date  : 2017/7/13 15:42
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class CloseServerGM implements GM{

    @Override
    public String executeGM(UserSession session, String[] gmStr) {
        new GameCloseThread((short) 1,GameCloseThread.SourceType.GM_COMMAND,session).run();
        return "己执行";
    }
}
