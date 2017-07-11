package info.xiaomo.server.listener;

import info.xiaomo.server.entify.Player;
import info.xiaomo.server.event.EventType;
import info.xiaomo.server.event.IListener;

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
 * Date  : 2017/7/11 19:27
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class SecondListener implements IListener {
    @Override
    public void update(EventType type, Object param) {
        Player player = (Player)param;
        System.out.println("登录事件。。。。。");
    }
}
