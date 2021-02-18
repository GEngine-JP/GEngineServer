package info.xiaomo.server.fishscript.room.handler;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.fish.manager.RoleManager;
import info.xiaomo.server.fish.manager.RoomManager;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.protocol.gameserver.room.EnterRoomRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 进入房间
 * <p>
 * <p>
 * 2017年7月5日 下午5:35:36
 */
@HandlerEntity(mid = MsgId.EnterRoomReq_VALUE, msg = EnterRoomRequest.class)
public class EnterRoomHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnterRoomHandler.class);
//	private static AtomicInteger num = new AtomicInteger(0);

	@Override
	public void run() {
//		LOGGER.warn("{}", getMessage().toString());
		EnterRoomRequest req = getMsg();
		UserRole role = RoleManager.getInstance().getRole(rid);
		if (role == null) {
			LOGGER.warn("角色{}未登陆", rid);
			return;
		}
		RoomManager.getInstance().enterRoom(role, req.getType(), req.getRank());
//		EnterRoomResponse.Builder builder = EnterRoomResponse.newBuilder();
//		builder.setResult(num.getAndIncrement());
//		sendIdMsg(builder.build());

	}

}
