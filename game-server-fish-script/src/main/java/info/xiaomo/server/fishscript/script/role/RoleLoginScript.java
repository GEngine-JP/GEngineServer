package info.xiaomo.server.fishscript.script.role;

import java.util.Map;
import java.util.function.Consumer;

import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.key.HallKey;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.fish.manager.RoleManager;
import info.xiaomo.server.fish.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 登录
 * 
 *
 *  2017年8月4日 下午2:14:53
 */
public class RoleLoginScript implements IRoleScript {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleLoginScript.class);

	@Override
	public void login(long roleId, GlobalReason reason, Consumer<UserRole> roleConsumer) {
		UserRole role = RoleManager.getInstance().loadRoleData(roleId);
		if (role == null) {
			role = new UserRole();
			role.setId(roleId);
		}
		role.setGameId(Config.SERVER_ID);
		if (roleConsumer != null) {
			roleConsumer.accept(role);
		}
		RoleManager.getInstance().getOnlineRoles().put(roleId, role);

		// 同步大厅角色数据，昵称、头像等
		syncHallData(role);
		tempInit(role);
	}

	/**
	 * 同步大厅数据
	 * 
	 *
	 *  2017年9月26日 下午2:44:18
	 * @param role
	 */
	private void syncHallData(UserRole role) {
		//同步角色
		String key = HallKey.Role_Map_Info.getKey(role.getId());
		Map<String, String> hgetAll = JedisManager.getJedisCluster().hgetAll(key);
		if (hgetAll == null) {
			LOGGER.warn("{}为找到角色数据", key);
			return;
		}
		UserRole hallRole = new UserRole();
		JsonUtil.map2Object(hgetAll, hallRole);
		role.setNick(hallRole.getNick());
		role.setGold(hallRole.getGold());
		role.setLevel(hallRole.getLevel());
		RoleManager.getInstance().saveRoleData(role);

		
//		//加载大厅数据
//		// 道具
//		RMap<Long, Item> items = RedissonManager.getRedissonClient()
//				.getMap(HallKey.Role_Map_Packet.getKey(role.getId()),new FastJsonCodec(Long.class,Item.class));
//		role.setItems(items);

	}

	/**
	 * 临时初始化
	 * 
	 *
	 *  2017年9月25日 下午5:31:37
	 * @param role
	 */
	private void tempInit(UserRole role) {
		// 赠送金币
		if (role.getGold() < 100) {
//			role.changeGold(100000, Reason.RoleFire);
		}
	}

}
