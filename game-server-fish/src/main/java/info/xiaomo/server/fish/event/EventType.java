package info.xiaomo.server.fish.event;

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
 * Date  : 2017/7/11 19:24
 * desc  : 事件类型
 * Copyright(©) 2017 by xiaomo.
 */
public enum EventType {
    /**
     * 登录
     */
    LOGIN,
    /**
     * 登出
     */
    LOGOUT,
    /**
     * 服务器分钟心跳
     */
    SERVER_MINUTE_HEART,
    /**
     * 服务器零点事件
     */
    SERVER_MIDNIGHT,
    /**
     * 服务器秒心跳
     */
    SERVER_SECOND_HEART,
    /**
     * 关服事件
     */
    SERVER_SHUTDOWN

}
