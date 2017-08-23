package info.xiaomo.server.back;


import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.gameCore.protocol.MessagePool;

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
public class BackMessagePool implements MessagePool {

    // 消息类字典
    private final Map<Integer, Class<? extends Message>> messages = new HashMap<>();
    // 处理类字典
    private final Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>();


    public BackMessagePool() {

    }

    @Override
    public Message getMessage(int messageId) {
        Class<?> clazz = messages.get(messageId);
        if (clazz != null) {
            try {
                return (Message) clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void register(int messageId, Class<? extends Message> messageClazz) {
        try {
            messages.put(messageId, messageClazz);
        } catch (Exception e) {
            throw new RuntimeException("消息注册错误....");
        }
    }

}
