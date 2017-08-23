package info.xiaomo.server.server;

import info.xiaomo.gameCore.protocol.Message;

/**
 * message执行过滤器,可以过滤掉一些特殊条件
 *
 * @author Administrator
 */
public interface MessageFilter {

    /**
     * message执行逻辑之前调用
     *
     * @return
     */
    boolean before(Message msg);

    /**
     * message执行逻辑之后调用
     *
     * @param msg
     * @return
     */
    boolean after(Message msg);
}
