package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.User;

/**
 * 用户接口
 * <p>
 * <p>
 * 2017年7月7日 下午4:16:57
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
