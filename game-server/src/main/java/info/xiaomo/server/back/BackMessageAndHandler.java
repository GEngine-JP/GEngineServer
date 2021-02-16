package info.xiaomo.server.back;


import com.google.protobuf.AbstractMessage;

import java.util.HashMap;
import java.util.Map;
import info.xiaomo.gengine.network.netty.AbstractHandler;
import info.xiaomo.gengine.network.netty.IMessageAndHandler;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/11 16:00
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class BackMessageAndHandler implements IMessageAndHandler {


    /**
     * 消息类字典
     */
    private final Map<Integer, AbstractMessage> messages = new HashMap<>(10);
    private final Map<String, Integer> ids = new HashMap<>(10);

    public BackMessageAndHandler() {
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
        return null;
    }

    @Override
    public void register(int messageId, AbstractMessage messageClazz, Class<? extends AbstractHandler> handler) {

    }

    @Override
    public void register(int messageId, AbstractMessage messageClazz) {

    }


}
