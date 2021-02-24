package info.xiaomo.server.rpg.event.listener;

import info.xiaomo.gengine.event.IEventListener;
import info.xiaomo.server.rpg.entify.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把今天最好的表现当作明天最新的起点．．～ いま 最高の表現 として 明日最新の始発．．～ Today the best performance as tomorrow newest
 * starter! Created by IntelliJ IDEA.
 *
 * <p>
 *
 * @author : xiaomo github: https://github.com/xiaomoinfo email : xiaomo@xiaomo.info QQ : 83387856
 *     Date : 2017/7/11 19:27 desc : Copyright(©) 2017 by xiaomo.
 */
public class LogoutEventListener implements IEventListener {
    public static final Logger LOGGER = LoggerFactory.getLogger(LogoutEventListener.class);

    @Override
    public void update(Integer type, Object param) {
        User user = (User) param;
        LOGGER.warn("用户 {} 退出登录", user.getLoginName());
    }
}
