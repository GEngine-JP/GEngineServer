package info.xiaomo.server.back;

import info.xiaomo.core.net.Message;
import info.xiaomo.core.net.MessagePool;

import java.util.HashMap;
import java.util.Map;

public class BackMessagePool implements MessagePool {

    private Map<Integer, Class<? extends Message>> pool = new HashMap<>();

    /**
     * 注册消息
     * 如果新加模块 则添加一个新方法进来
     */
    public BackMessagePool() {
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

    public void register(int messageId, Class<? extends Message> clazz) {
        pool.put(messageId, clazz);
    }

}
