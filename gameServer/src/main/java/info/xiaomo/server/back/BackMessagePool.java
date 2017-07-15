package info.xiaomo.server.back;

import info.xiaomo.core.net.Message;
import info.xiaomo.core.net.MessagePool;
import info.xiaomo.server.back.msg.ReqCloseServerMessage;
import info.xiaomo.server.back.msg.ReqExecGMMessage;
import info.xiaomo.server.back.msg.ReqReloadCfgMessage;

import java.util.HashMap;
import java.util.Map;

public class BackMessagePool implements MessagePool {

    private Map<Integer, Class<? extends Message>> pool = new HashMap<>();

    /**
     * 注册消息
     * 如果新加模块 则添加一个新方法进来
     */
    public BackMessagePool() {
    	register(1001, ReqCloseServerMessage.class);
    	register(1003, ReqExecGMMessage.class);
    	register(1005, ReqReloadCfgMessage.class);
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
