package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.common.bean.Reason;
import info.xiaomo.gengine.common.struct.Role;
import info.xiaomo.gengine.script.IScript;


/**
 * 玩家脚本
 *
 * 
 * @date 2017-03-30
 */
public interface IRoleScript extends IScript {

	/**
	 * 登录游戏
	 */
	public default void login(Role role, Reason reason) {

	}

	/**
	 * 创建角色
	 * 
	 * @param userId
	 * @return
	 */
	public default Role createRole(long userId, Consumer<Role> roleConsumer) {

		return null;
	}
	
	/**
	 * 角色退出游戏
	 * 
	 * 
	 * 2017年9月18日 下午6:01:08
	 * @param role
	 */
	default void quit(Role role,Reason reason) {
		
	}
	
}
