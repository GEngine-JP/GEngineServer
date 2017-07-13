package info.xiaomo.server.system.user;

import info.xiaomo.server.entify.User;
import info.xiaomo.server.event.EventType;
import info.xiaomo.server.event.EventUtil;

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
 * Date  : 2017/7/13 15:02
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class UserManager {
    private static UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {
    }

    public void login(User user) {
        EventUtil.executeEvent(EventType.LOGIN, user);
    }

    public void logout(User user) {
        EventUtil.executeEvent(EventType.LOGOUT, user);
    }
}
