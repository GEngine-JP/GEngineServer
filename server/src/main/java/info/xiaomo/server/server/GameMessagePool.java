package info.xiaomo.server.server;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.gameCore.protocol.AbstractHandler;
import info.xiaomo.gameCore.protocol.MessagePool;
import info.xiaomo.server.protocol.UserProto.LoginRequest;
import info.xiaomo.server.system.user.handler.LoginHandler;

import java.util.HashMap;
import java.util.Map;

public class GameMessagePool implements MessagePool {

    // 消息类字典
    private final Map<Integer, AbstractMessage> messages = new HashMap<>();
    private final Map<String, Integer> ids = new HashMap<>();

    private final Map<String, Class<? extends AbstractHandler>> handlers = new HashMap<>();

    public GameMessagePool() {
        register(101101, LoginRequest.getDefaultInstance(), LoginHandler.class);
    }

    @Override
    public AbstractMessage getMessage(int messageId) {
        return messages.get(messageId);
    }

    @Override
    public int getMessageId(AbstractMessage message) {
        return ids.get(message.getClass().getName());
    }


    @Override
    public AbstractHandler getHandler(String handlerName) {
        Class<? extends AbstractHandler> clazz = handlers.get(handlerName);
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler) {
        messages.put(messageId, messageClazz);
        handlers.put(messageClazz.getClass().getName(), handler);
        ids.put(messageClazz.getClass().getName(), messageId);
    }
}
