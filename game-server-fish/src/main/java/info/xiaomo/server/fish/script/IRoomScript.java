package info.xiaomo.server.fish.script;

import java.time.LocalTime;
import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.entity.room.Room;
import info.xiaomo.server.shared.protocol.gameserver.room.RoomType;

/**
 * 房间脚本
 * <p>
 * <p>
 * 2017-04-21
 */
public interface IRoomScript extends IScript {


	/**
	 * 进入房间
	 *
	 * @param role
	 * @param room
	 * @return
	 */
	default void enterRoom(UserRole role, Room room) {

	}

	/**
	 * 进入房间
	 *
	 * @param role
	 * @param roomType 房间类型
	 * @param rank     级别
	 *                 <p>
	 *                 Role> 2017年9月14日 下午2:42:25
	 */
	default void enterRoom(UserRole role, RoomType roomType, int rank) {

	}

	/**
	 * 退出房间
	 *
	 * @param role
	 * @param room
	 */
	default void quitRoom(UserRole role, Room room) {

	}

	/**
	 * 跑马灯
	 * <p>
	 * 没有辛运星
	 * </p>
	 *
	 * @param role
	 * @param accumulateGold
	 */
	default void sendPmd(UserRole role, int totalGold, int accumulateGold, int multiple, String fishName) {

	}

	/**
	 * 销毁房间
	 * <p>
	 * <p>
	 * Role> 2017年9月14日 上午9:47:12
	 */
	default void destroyRoom(Room iRoom) {

	}

	/**
	 * 每秒执行
	 *
	 * @param localTime Role>
	 *                  2017年9月26日 下午2:07:00
	 */
	default void secondHandler(Room room, LocalTime localTime) {

	}

	/**
	 * 每分钟执行
	 *
	 * @param localTime Role>
	 *                  2017年9月26日 下午1:51:45
	 */
	default void minuteHandler(Room room, LocalTime localTime) {

	}

	/**
	 * 每小时执行
	 *
	 * @param localTime Role>
	 *                  2017年9月26日 下午1:51:59
	 */
	default void hourHandler(Room room, LocalTime localTime) {

	}
}
