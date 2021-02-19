package info.xiaomo.server.fishscript.fight.handler;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.utils.MsgUtil;
import info.xiaomo.gengine.utils.TimeUtil;
import info.xiaomo.server.fish.manager.RoomManager;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.entity.room.Room;
import info.xiaomo.server.shared.protocol.gameserver.fight.FireRequest;
import info.xiaomo.server.shared.protocol.gameserver.fight.FireResponse;
import info.xiaomo.server.shared.protocol.msg.MsgId;

/**
 * 开炮
 */
@HandlerEntity(mid = MsgId.FireReq_VALUE, msg = FireRequest.class, thread = ThreadType.ROOM)
public class FireHandler extends TcpHandler {

	@Override
	public void run() {
		FireRequest req = getMsg();
		UserRole userRole = getPerson();

		Room room = RoomManager.getInstance().getRoom(userRole.getRoomId());

		if (userRole.getGold() < req.getGold()) {
			return;
		}

		userRole.getFireGolds().add(req.getGold());
//        RoleUtil.changeGold(-req.getGold(), GlobalReason.RoleFire);
//        RoleUtil.addBetGold(req.getGold());
		userRole.setFireTime(TimeUtil.currentTimeMillis());
//        RoleUtil.addFireCount();


		FireResponse.Builder builder = FireResponse.newBuilder();
		builder.setRid(userRole.getId());
		builder.setGold(req.getGold());
		builder.setAngleX(req.getAngleX());
		builder.setAngleY(req.getAngleY());
		builder.setTargetFishId(req.getTargetFishId());
		FireResponse response = builder.build();
		room.getRoles().values().forEach(roomRole -> MsgUtil.sendMsg(roomRole, response));

	}

}
