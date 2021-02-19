package info.xiaomo.server.fishscript.fight.handler;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.shared.protocol.gameserver.fight.UseSkillRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;

/**
 * 使用技能，道具
 * 下午2:44:50
 */
@HandlerEntity(mid = MsgId.UseSkillReq_VALUE, msg = UseSkillRequest.class)
public class UseSkillHandler extends TcpHandler {

	@Override
	public void run() {

	}

}
