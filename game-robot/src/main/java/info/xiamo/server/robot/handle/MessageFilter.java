package info.xiamo.server.robot.handle;

import com.google.protobuf.AbstractMessage;

/**
 * message执行过滤器,可以过滤掉一些特殊条件
 * 
 * @author Administrator
 * 
 */
public interface MessageFilter {

	/**
	 * message执行逻辑之前调用
	 * 
	 * @return
	 */
	boolean before(AbstractMessage msg);

	/**
	 * message执行逻辑之后调用
	 * @param msg
	 * @return
	 */
	boolean after(AbstractMessage msg);
}
