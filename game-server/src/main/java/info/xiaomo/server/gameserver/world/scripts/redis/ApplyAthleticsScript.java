package info.xiaomo.server.gameserver.world.scripts.redis;

import info.xiaomo.gengine.persist.redis.IPubSubScript;
import info.xiaomo.gengine.persist.redis.channel.BydrChannel;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.jedis.JedisPubSubMessage;
import info.xiaomo.gengine.persist.redis.key.HallKey;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.gameserver.world.BydrWorldChannel;
import info.xiaomo.server.gameserver.world.TeamManager;
import info.xiaomo.server.gameserver.manager.ConfigManager;
import info.xiaomo.server.shared.entity.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 申请竞技赛
 * 
 *
 *  2017年8月3日 上午11:07:52
 */
public class ApplyAthleticsScript implements IPubSubScript {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyAthleticsScript.class);

	@Override
	public void onMessage(String channel, JedisPubSubMessage message) {
		if (!BydrWorldChannel.ApplyAthleticsReq.name().equals(channel)) {
			return;
		}
		long roleId = message.getId();
		int type = message.getServer();
		int rank = message.getTarget();
		LOGGER.info("角色{}申请竞技赛", roleId);

		Team team = TeamManager.getInstance().getIdleTeam(rank);
		String gameIdStr = JedisManager.getJedisCluster().hget(HallKey.Role_Map_Info.getKey(roleId), "gameId");
		if (gameIdStr == null) {
			LOGGER.warn("角色{} 所在服务器信息不全", roleId);
			return;
		}
		//人数已满判断
		if (team == null||team.getRoleIds().size()>= ConfigManager.getInstance().getGameConfig().getRoomSize()) {
			team = new Team(roleId, Integer.parseInt(gameIdStr), rank);
			TeamManager.getInstance().getTeams().put(team.getId(), team);
		}
		
		team.getRoleIds().add(roleId);
		
		JedisPubSubMessage resMsg=new JedisPubSubMessage();
		resMsg.setIds(team.getRoleIds());
		resMsg.setJson(JsonUtil.toJSONString(team));
		resMsg.setServer(team.getServerId());
		
		//TODO 匹配完成后队伍是否还需要存在？删除队伍等操作......(玩家进入房间后，通知销毁)
		
		JedisManager.getJedisCluster().publish(BydrChannel.ApplyAthleticsRes.name(), resMsg.toString());
		team.saveToRedis();
	}

}
