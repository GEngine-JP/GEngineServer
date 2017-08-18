package info.xiaomo.server.server;

import info.xiaomo.gameCore.base.AbstractHandler;
import info.xiaomo.gameCore.protocol.HandlerPool;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.gameCore.protocol.message.AbstractMessage;


import java.util.HashMap;
import java.util.Map;

public class MessageAndHandlerPool implements MessagePool, HandlerPool {

    // 消息类字典
    private final Map<Integer, Class<? extends AbstractMessage>> messages = new HashMap<>();
    // 处理类字典
    private final Map<Integer, Class<? extends AbstractHandler>> handlers = new HashMap<>();

    public MessageAndHandlerPool() {
        //心跳

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

    public void register(int messageId, Class<? extends AbstractMessage> messageClazz,
                         Class<? extends AbstractHandler> handlerClazz) {
        try {
            messages.put(messageId, messageClazz);
            handlers.put(messageId, handlerClazz);
        } catch (Exception e) {
            throw new RuntimeException("消息注册错误....");
        }
    }
}
