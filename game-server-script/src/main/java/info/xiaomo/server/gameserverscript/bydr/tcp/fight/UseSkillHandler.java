package info.xiaomo.server.gameserverscript.bydr.tcp.fight;

import info.xiaomo.gengine.network.handler.HandlerEntity;
import info.xiaomo.gengine.network.handler.TcpHandler;
import info.xiaomo.server.shared.protocol.gameserver.fight.UseSkillRequest;
import info.xiaomo.server.shared.protocol.msg.MsgId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用技能，道具
 *
 *
 * 2017年9月18日 下午2:44:50
 */
@HandlerEntity(mid= MsgId.UseSkillReq_VALUE,msg= UseSkillRequest.class)
public class UseSkillHandler extends TcpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyAthleticsHandler.class);

	@Override
	public void run() {
		
	}

}
