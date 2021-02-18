package info.xiaomo.server.fish.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.key.BydrKey;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.fish.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色管理
 *
 *
 * Role> 2017年7月10日 下午4:01:42
 */
public class RoleManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleManager.class);
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
	 *
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
	 *
	 * Role> 2017年8月3日 下午3:21:15
	 */
	public void quit(UserRole role, GlobalReason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class,
				script -> script.quit(role, reason));
	}

	/**
	 * 加载角色数据
	 *
	 * @param roleId
	 *
	 * Role> 2017年8月3日 下午3:43:59
	 */
	public UserRole loadRoleData(long roleId) {
		Map<String, String> roleMap = JedisManager.getJedisCluster().hgetAll(BydrKey.Role_Map.getKey(roleId));
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
	 *
	 * Role> 2017年8月3日 下午3:22:58
	 */
	public void saveRoleData(UserRole role) {
		String key = BydrKey.Role_Map.getKey(role.getId());
		LOGGER.debug("{}存储数据", key);
		JedisManager.getJedisCluster().hmset(key, JsonUtil.object2Map(role));
		//
	}
}
