package info.xiaomo.server.back;


import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.HandlerPool;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;
import info.xiaomo.server.protocol.gm.handler.ReqCloseServerHandler;
import info.xiaomo.server.protocol.gm.message.ReqCloseServerMessage;

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
public class BackMessagePool implements MessagePool, HandlerPool {

    // 消息类字典
    private final Map<Integer, Class<? extends AbstractMessage>> messages = new HashMap<>();
    // 处理类字典
    private final Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>();


    public BackMessagePool() {
        register(2201, ReqCloseServerMessage.class, ReqCloseServerHandler.class);


    }

    @Override
    public AbstractMessage getMessage(int messageId) {
        Class<?> clazz = messages.get(messageId);
        if (clazz != null) {
            try {
                return (AbstractMessage) clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void register(int messageId, Class<? extends AbstractMessage> messageClazz, Class<? extends AbstractHandler> handlerClazz) {
        try {
            messages.put(messageId, messageClazz);
            handlers.put(messageId, handlerClazz);
        } catch (Exception e) {
            throw new RuntimeException("消息注册错误....");
        }
    }

    @Override
    public AbstractHandler getHandler(int messageId) {
        Class<?> clazz = handlers.get(messageId);
        if (clazz != null) {
            try {
                return (AbstractHandler) clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
