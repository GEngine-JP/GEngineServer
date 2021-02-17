package info.xiaomo.server.script;

import java.util.function.Consumer;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.struct.role.Role;

/**
 * 机器人脚本
 *
 *
 * @date 2017-04-27
 *
 */
public interface IRobotScript extends IScript {

	/**
	 * 创建机器人
	 *
	 * @param roleConsumer
	 */
	default Role createRobot(Consumer<Role> roleConsumer) {
		return null;
	}

	/**
	 * 检查机器人金币，并修正
	 */
	default void checkGold(Role robot) {

	}
}
