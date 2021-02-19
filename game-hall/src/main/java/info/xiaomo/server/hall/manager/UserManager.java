package info.xiaomo.server.hall.manager;

import java.util.Collection;
import java.util.function.Consumer;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.server.hall.script.IUserScript;
import info.xiaomo.server.shared.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户管理
 *
 * <p>2017年7月7日 下午4:00:13
 */
public class UserManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);
	private static volatile UserManager userManager;

	private UserManager() {
	}

	public static UserManager getInstance() {
		if (userManager == null) {
			synchronized (UserManager.class) {
				if (userManager == null) {
					userManager = new UserManager();
				}
			}
		}
		return userManager;
	}

	/**
	 * 创建角色
	 *
	 * @param userConsumer
	 * @return
	 */
	public User createUser(Consumer<User> userConsumer) {
		Collection<IUserScript> evts =
				ScriptManager.getInstance().getBaseScriptEntry().getEvts(IUserScript.class);
		for (IUserScript userScript : evts) {
			User user = userScript.createUser(userConsumer);
			if (user != null) {
				return user;
			}
		}
		return null;
	}
}
