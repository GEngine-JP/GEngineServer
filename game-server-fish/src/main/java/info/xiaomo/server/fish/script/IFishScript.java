package info.xiaomo.server.fish.script;


import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.shared.entity.room.Room;

/**
 * 鱼脚本
 *
 *
 * 2017-04-24
 */
public interface IFishScript extends IScript {

	/**
	 * 刷新鱼
	 *
	 * @param room
	 */
	default void fishRefresh(Room room) {

	}

	/**
	 * 鱼死亡
	 *
	 * @param room
	 */
	default void fishDie(Room room) {

	}


}
