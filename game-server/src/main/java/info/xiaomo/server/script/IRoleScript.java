package info.xiaomo.server.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.struct.role.Role;

/**
 * 角色
 *
 *
 * Role>
 * 2017年8月4日 下午2:10:47
 */
public interface IRoleScript extends IScript {

	/**
	 * 登录
	 *
	 * @param roleId
	 * @param reason
	 * @param roleConsumer
	 *
	 * Role>
	 * 2017年8月4日 下午2:12:33
	 */
	default void login(long roleId, GlobalReason reason, Consumer<Role> roleConsumer) {

	}

	/**
	 * 退出
	 *
	 * @param reason
	 *
	 * Role>
	 * 2017年8月4日 下午2:12:40
	 */
	default void quit(Role role, GlobalReason reason) {

	}

	/**
	 * 修改金币
	 *
	 * @param role
	 * @param gold
	 * @param reason
	 *
	 * Role>
	 * 2017年9月25日 下午5:20:33
	 */
	default void changeGold(Role role, int gold, GlobalReason reason) {

	}

	/**
	 * 奖金币同步大大厅
	 *
	 * @param role
	 * @param reason
	 *
	 * Role>
	 * 2017年9月26日 上午10:40:51
	 */
	default void syncGold(Role role, GlobalReason reason) {

	}
}
