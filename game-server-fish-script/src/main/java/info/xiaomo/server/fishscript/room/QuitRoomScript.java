package info.xiaomo.server.fishscript.room;

import info.xiaomo.server.fish.script.IRoomScript;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.entity.room.Room;

/**
 * 退出房间
 * 
 *
 *  2017年9月14日 上午10:10:12
 */
public class QuitRoomScript implements IRoomScript {

	@Override
	public void quitRoom(UserRole role, Room room) {
		room.getRoles().values().removeIf(userRole -> userRole.getId() == role.getId());
	}

}
