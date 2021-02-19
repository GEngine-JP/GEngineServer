package info.xiaomo.server.fish.manager;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.fish.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.rediskey.FishKey;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色管理
 * <p>
 * <p>
 * Role> 2017年7月10日 下午4:01:42
 */
@Slf4j
public class RoleManager {
	private static volatile RoleManager roleManager;

	private final Map<Long, UserRole> onlineRoles = new ConcurrentHashMap<>(); // 在线的角色

	private RoleManager() {

	}

	public static RoleManager getInstance() {
		if (roleManager == null) {
			synchronized (RoleManager.class) {
				if (roleManager == null) {
					roleManager = new RoleManager();
				}
			}
		}
		return roleManager;
	}

	public Map<Long, UserRole> getOnlineRoles() {
		return onlineRoles;
	}

	public UserRole getRole(long roleId) {
		return onlineRoles.get(roleId);
	}

	/**
	 * 登录
	 *
	 * @param roleId
	 * @param reason
	 * @return 0 成功
	 * <p>
	 * Role> 2017年8月3日 下午3:49:37
	 */
	public void login(long roleId, GlobalReason reason, Consumer<UserRole> roleConsumer) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class,
				script -> script.login(roleId, reason, roleConsumer));
	}

	/**
	 * 退出
	 *
	 * @param reason 原因
	 *               <p>
	 *               Role> 2017年8月3日 下午3:21:15
	 */
	public void quit(UserRole role, GlobalReason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class,
				script -> script.quit(role, reason));
	}

	/**
	 * 加载角色数据
	 *
	 * @param roleId Role> 2017年8月3日 下午3:43:59
	 */
	public UserRole loadRoleData(long roleId) {
		Map<String, String> roleMap = JedisManager.getJedisCluster().hgetAll(FishKey.Role_Map.getKey(roleId));
		if (roleMap == null || roleMap.size() < 1) {
			return null;
		}
		UserRole role = new UserRole();
		JsonUtil.map2Object(roleMap, role);

		// TODO 其他角色数据

//		//大厅角色数据
//		RMap<String, Object> hallRole= RedissonManager.getRedissonClient().getMap(HallKey.Role_Map_Info.getKey(roleId), new StringCodec());
//		role.setHallRole(hallRole);

		return role;
	}

	/**
	 * 存储角色数据
	 *
	 * @param role TODO 存储到mongodb
	 *             <p>
	 *             Role> 2017年8月3日 下午3:22:58
	 */
	public void saveRoleData(UserRole role) {
		String key = FishKey.Role_Map.getKey(role.getId());
		log.debug("{}存储数据", key);
		JedisManager.getJedisCluster().hmset(key, JsonUtil.object2Map(role));
	}


	public void addFishHitsCount(UserRole userRole, int configId) {
		userRole.getFishHits().merge(configId, 1, Integer::sum);
	}


	public void addFishDiesCount(UserRole userRole, int configId) {
		userRole.getFishDies().merge(configId, 1, Integer::sum);
	}


	/**
	 * 修改金币
	 *
	 * @param gold
	 * @param reason Role>
	 *               2017年9月25日 下午5:23:41
	 */
	public void changeGold(UserRole userRole, int gold, GlobalReason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.changeGold(userRole, gold, reason));
	}

	/**
	 * 同步金币
	 *
	 * @param reason Role>
	 *               2017年9月26日 上午10:42:24
	 */
	public void syncGold(UserRole userRole, GlobalReason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.syncGold(userRole, reason));
	}

	public void saveToRedis(UserRole userRole, String propertiesName) {
		if (userRole.getRoomId() < 1) {
			throw new RuntimeException(String.format("角色ID %d 异常", userRole.getRoomId()));
		}
		String key = FishKey.Role_Map.getKey(userRole.getRoomId());
		Method method = UserRole.WRITEMETHODS.get(propertiesName);
		if (method != null) {
			try {
				Object value = method.invoke(userRole);
				if (value != null) {
					JedisManager.getJedisCluster().hset(key, propertiesName, value.toString());
				} else {
					log.warn("属性{}值为null", propertiesName);
				}

			} catch (Exception e) {
				log.error("属性存储", e);
			}
		} else {
			log.warn("属性：{}未找到对应方法", propertiesName);
		}
	}
}
