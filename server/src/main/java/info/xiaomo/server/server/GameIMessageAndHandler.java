package info.xiaomo.server.server;

import com.google.protobuf.AbstractMessage;
import info.xiaomo.core.network.AbstractHandler;
import info.xiaomo.core.network.IMessageAndHandler;
import info.xiaomo.server.protocol.UserProto.LoginRequest;
import info.xiaomo.server.protocol.UserProto.LoginResponse;
import info.xiaomo.server.system.user.handler.LoginHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomo
 */
public class GameIMessageAndHandler implements IMessageAndHandler {

    // 消息类字典
    private final Map<Integer, AbstractMessage> messages = new HashMap<>(10);
    private final Map<String, Integer> ids = new HashMap<>(10);

    private final Map<String, Class<? extends AbstractHandler>> handlers = new HashMap<>(10);

    public GameIMessageAndHandler() {
        register(101101, LoginRequest.getDefaultInstance(), LoginHandler.class);
        register(101102, LoginResponse.getDefaultInstance());
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

    @Override
    public void register(int messageId, AbstractMessage messageClazz) {
        messages.put(messageId, messageClazz);
        ids.put(messageClazz.getClass().getName(), messageId);
    }
}
