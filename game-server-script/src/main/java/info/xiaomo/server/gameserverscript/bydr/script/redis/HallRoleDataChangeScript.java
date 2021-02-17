package info.xiaomo.server.gameserverscript.bydr.script.redis;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.persist.redis.IPubSubScript;
import info.xiaomo.gengine.persist.redis.jedis.JedisPubSubMessage;
import info.xiaomo.server.gameserver.manager.RoleManager;
import info.xiaomo.server.gameserver.world.BydrChannel;
import info.xiaomo.server.shared.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 大厅通知角色数据改变
 * 
 *
 *  2017年10月17日 上午10:20:00
 */
public class HallRoleDataChangeScript implements IPubSubScript {
	private static final Logger LOGGER=LoggerFactory.getLogger(HallRoleDataChangeScript.class);

	@Override
	public void onMessage(String channel, JedisPubSubMessage message) {
		if (!channel.startsWith("Hall")) { // channel必须以Hall开头
			return;
		}
		if(message.getServer()!= Config.SERVER_ID) {
			return;
		}
		UserRole role = RoleManager.getInstance().getRole(message.getId());
		if(role==null) {
			LOGGER.warn("角色[{}]已退出游戏:{} {}更新失败",message.getId(),channel,message.toString());
			return;
		}
		
		switch (BydrChannel.valueOf(channel)) {
		case HallGoldChange:
//			role.changeGold(message.getTarget(), Reason.HallGoldChange);
			break;

		default:
			break;
		}

	}

	
}
