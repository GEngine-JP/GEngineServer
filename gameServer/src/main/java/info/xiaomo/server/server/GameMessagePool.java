package info.xiaomo.server.server;


import info.xiaomo.core.net.Message;
import info.xiaomo.core.net.MessagePool;

import java.util.HashMap;
import java.util.Map;

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
 * Date  : 2017/7/11 16:00
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class GameMessagePool implements MessagePool {

    private Map<Integer, Class<? extends Message>> pool = new HashMap<>();

    /**
     * 在这里注册消息
     */
    public GameMessagePool() {
        registerUser();
    }


    /**
     * 注册用户
     */
    private void registerUser() {
//        register(messageId,xxx.class);
    }


    @Override
    public Message get(int messageId) {
        Class<? extends Message> clazz = pool.get(messageId);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private void register(int messageId, Class<? extends Message> clazz) {
        pool.put(messageId, clazz);
    }
}
