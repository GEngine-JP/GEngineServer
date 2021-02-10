package info.xiaomo.server.event;

import info.xiaomo.server.listener.LoginEventListener;
import info.xiaomo.server.listener.LogoutEventListener;
import info.xiaomo.server.listener.MinuteEventListener;
import info.xiaomo.server.listener.SecondEventListener;

import static info.xiaomo.server.event.EventUtil.addListener;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 *
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 19:23
 * desc  : 事件注册类
 * Copyright(©) 2017 by xiaomo.
 */
public class EventRegister {

    /**
     * 可以隨時新增事件
     */
    public static void registerPreparedListeners() {
        addLoginListener(); // 登录监听器
        addLogoutListener(); // 登出事件
        addServerSecondListener(); // 1秒钟一次的事件
        addServerMinuteListener(); // 1分钟一次的事件

    }

    private static void addLogoutListener() {
        addListener(new LogoutEventListener(), EventType.LOGOUT);
    }

    private static void addServerMinuteListener() {
        addListener(new MinuteEventListener(), EventType.SERVER_MINUTE_HEART);
    }

    private static void addServerSecondListener() {
        addListener(new SecondEventListener(), EventType.SERVER_SECOND_HEART);
    }

    /**
     * 登录事件监听器
     */
    private static void addLoginListener() {
        addListener(new LoginEventListener(), EventType.LOGIN);
    }
}
