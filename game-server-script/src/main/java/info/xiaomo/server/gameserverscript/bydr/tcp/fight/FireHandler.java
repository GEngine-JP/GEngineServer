package info.xiaomo.server.gameserverscript.bydr.tcp.fight;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.gengine.thread.ThreadType;
import info.xiaomo.gengine.utils.TimeUtil;
import info.xiaomo.server.gameserver.manager.RoomManager;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.entity.room.Room;
import info.xiaomo.server.shared.protocol.gameserver.fight.FireRequest;
import info.xiaomo.server.shared.protocol.gameserver.fight.FireResponse;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 开炮
 * <p>
 * <p>
 * 2017-04-21 QQ:359135103
 */
@HandlerEntity(mid = MsgId.FireReq_VALUE, msg = FireRequest.class, thread = ThreadType.ROOM)
public class FireHandler extends TcpHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FireHandler.class);

    @Override
    public void run() {
        FireRequest req = getMsg();
        // LOGGER.info(req.toString());
        UserRole userRole = getPerson();

        Room room = RoomManager.getInstance().getRoom(userRole.getRoomId());

        if (userRole.getGold() < req.getGold()) {
//			sendServerMsg(ServerMsgId.not_enough_gold);
            return;
        }

        userRole.getFireGolds().add(req.getGold());
//        RoleUtil.changeGold(-req.getGold(), GlobalReason.RoleFire);
//        RoleUtil.addBetGold(req.getGold());
        userRole.setFireTime(TimeUtil.currentTimeMillis());
//        room.addAllFireCount();
//        RoleUtil.addFireCount();


        FireResponse.Builder builder = FireResponse.newBuilder();
        builder.setRid(userRole.getId());
        builder.setGold(req.getGold());
        builder.setAngleX(req.getAngleX());
        builder.setAngleY(req.getAngleY());
        builder.setTargetFishId(req.getTargetFishId());
        FireResponse response = builder.build();
        room.getRoles().values().forEach(roomRole -> {
//            MsgUtil.sendMsg(response);
        });

    }

}
