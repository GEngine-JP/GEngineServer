package info.xiaomo.server.fishscript.role;

import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.server.fish.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.rediskey.HallKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 修改角色金币
 *
 *
 * 2017年9月25日 下午5:21:32
 */
public class RoleGoldChangeScript implements IRoleScript {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleGoldChangeScript.class);

	@Override
	public void changeGold(UserRole role, long add, GlobalReason reason) {
		long gold = role.getGold() + add;
		if (gold < 0 || gold > Long.MAX_VALUE) {
			LOGGER.warn("玩家更新金币异常,{}+{}={}", role.getGold(), add, gold);
			role.setGold(0);
		}
		role.setGold(gold);
		if (reason == GlobalReason.RoleFire) {
			role.setWinGold(role.getWinGold() + add);
		}
	}

	@Override
	public void syncGold(UserRole role, GlobalReason reason) {
		if (role.getWinGold() != 0) {
			String key = HallKey.Role_Map_Info.getKey(role.getId());

			long addAndGet = JedisManager.getJedisCluster().hincrBy(key, "gold", role.getWinGold());
			role.setWinGold(0);
			LOGGER.debug("更新后金币为{}", addAndGet);
		}

	}

}
