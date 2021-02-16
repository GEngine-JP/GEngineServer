package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.common.struct.User;
import info.xiaomo.gengine.script.IScript;

/**
 * 用户接口
 * 
 *
 *  2017年7月7日 下午4:16:57
 */
public interface IUserScript extends IScript {

	/**
	 * 创建用户
	 * 
	 * @param userConsumer
	 * @return
	 */
    public default User createUser(Consumer<User> userConsumer) {
		return null;
	}
}
