package info.xiaomo.server.fish.script;

import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.UserRole;

import java.util.function.Consumer;

/**
 * 机器人脚本
 * <p>
 * <p>
 * 2017-04-27
 */
public interface IRobotScript extends IScript {

    /**
     * 创建机器人
     *
     * @param roleConsumer
     */
    default UserRole createRobot(Consumer<UserRole> roleConsumer) {
        return null;
    }

    /**
     * 检查机器人金币，并修正
     */
    default void checkGold(UserRole robot) {

    }
}
