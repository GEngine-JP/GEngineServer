package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.UserRole;


/**
 * 玩家脚本
 *
 * 2017-03-30
 */
public interface IRoleScript extends IScript {

	/**
	 * 登录游戏
	 */
	public default void login(UserRole userRole, GlobalReason reason) {

	}

	/**
	 * 创建角色
	 *
	 * @param userId
	 * @return
	 */
	public default UserRole createRole(long userId, Consumer<UserRole> roleConsumer) {

		return null;
	}

	/**
	 * 角色退出游戏
	 *
	 *
	 *  下午6:01:08
	 *
	 * @param userRole
	 */
	default void quit(UserRole userRole, GlobalReason reason) {

	}

}
