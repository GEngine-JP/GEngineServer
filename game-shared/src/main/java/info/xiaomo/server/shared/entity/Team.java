package info.xiaomo.server.shared.entity;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Set;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.shared.rediskey.FishKey;
import lombok.Data;
import org.apache.mina.util.ConcurrentHashSet;

/**
 * 组队
 *
 * Role>
 * 2017年8月3日 上午11:24:51
 */
@Data
public class Team {

	/**
	 * 唯一ID，用队长ID作为ID
	 */
	@JSONField()
	private long id;

	/**
	 * 所在服务器ID，队长所在服务器
	 */
	@JSONField()
	private int serverId;

	/**
	 * 类型，当前只有竞技场比赛
	 */
	@JSONField()
	private int type;

	/**
	 * 级别
	 */
	@JSONField()
	private int rank;

	/**
	 * 队员集合
	 */
	@JSONField()
	private Set<Long> roleIds = new ConcurrentHashSet<>();

	/**
	 * 状态
	 */
	@JSONField()
	private int status;

	public Team(long id, int serverId, int rank) {
		this.id = id;
		this.serverId = serverId;
		this.rank = rank;
		roleIds.add(id);
	}


	public void saveToRedis() {
		JedisManager.getJedisCluster().hset(FishKey.Team_Map.getKey(), String.valueOf(id), JsonUtil.toJSONString(this));
	}
}
