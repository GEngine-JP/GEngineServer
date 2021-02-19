package info.xiaomo.server.hall.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.jedis.JedisPubSubMessage;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.hall.HallChannel;
import info.xiaomo.server.hall.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;
import info.xiaomo.server.shared.rediskey.HallKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色管理
 *
 * <p>2017年7月7日 下午4:00:37
 */
public class RoleManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleManager.class);
	private static volatile RoleManager roleManager;

	/**
	 * role 数据需要实时存数据库
	 */
	private final Map<Long, UserRole> roles = new ConcurrentHashMap<>();

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

	/**
	 * 创建角色
	 */
	public UserRole createUser(long userId, Consumer<UserRole> roleConsumer) {
		Collection<IRoleScript> evts =
				ScriptManager.getInstance().getBaseScriptEntry().getEvts(IRoleScript.class);
		Iterator<IRoleScript> iterator = evts.iterator();
		while (iterator.hasNext()) {
			IRoleScript userScript = iterator.next();
			UserRole userRole = userScript.createRole(userId, roleConsumer);
			if (userRole != null) {
				return userRole;
			}
		}
		return null;
	}

	public Map<Long, UserRole> getRoles() {
		return roles;
	}

	public UserRole getRole(long id) {
		UserRole userRole = roles.get(id);
		Map<String, String> hgetAll = JedisManager.getJedisCluster().hgetAll(userRole.getRoleRedisKey());
		// 从redis读取最新数据
		if (hgetAll != null) {
			JsonUtil.map2Object(hgetAll, userRole);
		}
		return userRole;
	}

	/**
	 * 登陆
	 *
	 * <p>2017年9月18日 下午6:23:14
	 *
	 * @param userRole
	 */
	public void login(UserRole userRole, GlobalReason reason) {
		ScriptManager.getInstance()
				.getBaseScriptEntry()
				.executeScripts(IRoleScript.class, script -> script.login(userRole, reason));
	}

	/**
	 * 退出
	 *
	 * <p>2017年9月18日 下午6:28:51
	 *
	 * @param rid
	 * @param reason
	 */
	public void quit(long rid, GlobalReason reason) {
		quit(getRole(rid), reason);
	}

	/**
	 * 退出游戏
	 *
	 * <p>2017年9月18日 下午6:09:51
	 *
	 * @param userRole
	 * @param reason
	 */
	public void quit(UserRole userRole, GlobalReason reason) {
		ScriptManager.getInstance()
				.getBaseScriptEntry()
				.executeScripts(IRoleScript.class, script -> script.quit(userRole, reason));
	}

	/**
	 * 广播金币改变
	 *
	 * <p>2017年10月17日 上午10:11:59
	 *
	 * @param roleId
	 * @param gold   金币改变量
	 */
	public void publishGoldChange(long roleId, int gold) {
		String gameIdStr =
				JedisManager.getJedisCluster().hget(HallKey.Role_Map_Info.getKey(roleId), "gameId");
		if (gameIdStr != null && !"0".equals(gameIdStr)) {
			JedisPubSubMessage message =
					new JedisPubSubMessage(roleId, Integer.parseInt(gameIdStr), gold);
			JedisManager.getJedisCluster().publish(HallChannel.HallGoldChange.name(), message.toString());
		}
	}
}
