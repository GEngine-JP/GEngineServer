package info.xiaomo.server.shared.rediskey;

/**
 * 捕鱼达人redis数据key枚举
 */
public enum FishKey {

	/**
	 * 角色基本信息
	 */
	Team_Map("Fish:Team:Map"),
	/**
	 * 角色信息
	 */
	Role_Map("Fish:Role_%d:Map");

	private final String key;

	FishKey(String key) {
		this.key = key;
	}

	public String getKey(Object... objects) {
		return String.format(key, objects);
	}
}
