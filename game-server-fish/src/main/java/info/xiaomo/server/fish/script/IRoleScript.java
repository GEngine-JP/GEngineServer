package info.xiaomo.server.fish.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.UserRole;

/**
 * 角色
 * <p>
 * <p>
 * Role>
 * 2017年8月4日 下午2:10:47
 */
public interface IRoleScript extends IScript {

	/**
	 * 登录
	 *
	 * @param roleId
	 * @param reason
	 * @param roleConsumer Role>
	 *                     2017年8月4日 下午2:12:33
	 */
	default void login(long roleId, GlobalReason reason, Consumer<UserRole> roleConsumer) {

	}

	/**
	 * 退出
	 *
	 * @param reason Role>
	 *               2017年8月4日 下午2:12:40
	 */
	default void quit(UserRole userRole, GlobalReason reason) {

	}

	/**
	 * 修改金币
	 *
	 * @param userRole
	 * @param gold
	 * @param reason   Role>
	 *                 2017年9月25日 下午5:20:33
	 */
	default void changeGold(UserRole userRole, long gold, GlobalReason reason) {

	}

	/**
	 * 奖金币同步大大厅
	 *
	 * @param userRole
	 * @param reason   Role>
	 *                 2017年9月26日 上午10:40:51
	 */
	default void syncGold(UserRole userRole, GlobalReason reason) {

	}
}
