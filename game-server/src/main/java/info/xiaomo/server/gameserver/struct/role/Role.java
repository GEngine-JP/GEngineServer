package info.xiaomo.server.gameserver.struct.role;

import java.lang.reflect.Method;
import java.util.*;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.entity.AbsPerson;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.key.BydrKey;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.ReflectUtil;
import info.xiaomo.server.gameserver.script.IRoleScript;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 角色信息
 *
 *
 * @date 2017-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(value = "role", noClassnameStored = true)
public class Role extends AbsPerson {
	private static final Logger LOGGER = LoggerFactory.getLogger(Role.class);

	/**
	 * setter 方法集合
	 */
	protected static final transient Map<String, Method> WRITEMETHODS = ReflectUtil.getReadMethod(Role.class);

	private String head;
	private int gender;
	private boolean is_vip;
	private String sign; // 签名
	private Date update_date;
	private Date cdate;
	private boolean is_sync_from_hall; // 是否已经和大厅同步

	private int giftGold; // 新手赠送金币
	private volatile long dayAwardGold; // 每日领奖金币
	private volatile long dayFireGold; // 每日开炮金币
	private int pay_rmb; // 充值额
	private long winGold; // 输赢的钱，需要同步更新到大厅数据库
	private long roomId; // 房间ID
	private int seatNo; // 座位编号
	private int roomType; // 房间类型
	@Embedded
	private List<Integer> presentFishs = new ArrayList<>(); // 赠送鱼，百分之百命中

	@Embedded
	private List<Integer> presentAccumulates = new ArrayList<>(); // 赠送累积奖，值为
	// 房间类型*10+0|1
	// 0从真池获取，1从假池获取
	@Embedded
	private List<Integer> fireGolds = new ArrayList<>(); // 开炮金币数
	private int betGold; // 当前下注金币总和
	private long enterRoomGold; // 进入房间时玩家的金币
	private long fireTime; // 玩家最后开炮时间

	// 玩家习惯统计
	private transient int fireCount; // 开炮次数
	private transient Map<Integer, Integer> fishHits = new HashMap<>(); // 命中鱼统计
	private transient Map<Integer, Integer> fishDies = new HashMap<>(); // 死亡鱼统计

	private transient int ownerId; // 机器人所属玩家ID
	private transient long leaveTime; // 机器人离开房间时间

	public void addFishHitsCount(int configId) {
		Integer count = fishHits.get(configId);
		if (count == null) {
			fishHits.put(configId, 1);
		} else {
			fishHits.put(configId, count + 1);
		}
	}

	public Map<Integer, Integer> getFishDies() {
		return fishDies;
	}

	public void setFishDies(Map<Integer, Integer> fishDies) {
		this.fishDies = fishDies;
	}

//	public Map<Long, Item> getItems() {
//		return items;
//	}
//
//	public void setItems(Map<Long, Item> items) {
//		this.items = items;
//	}


//	public RMap<String, Object> getHallRole() {
//		if(hallRole==null) {
//			this.hallRole= RedissonManager.getRedissonClient().getMap(HallKey.Role_Map_Info.getKey(this.id), new StringCodec());
//		}
//		return hallRole;
//	}
//
//	public void setHallRole(RMap<String, Object> hallRole) {
//		this.hallRole= hallRole;
//	}

	public void addFishDiesCount(int configId) {
		Integer count = fishDies.get(configId);
		if (count == null) {
			fishDies.put(configId, 1);
		} else {
			fishDies.put(configId, count + 1);
		}
	}


	/**
	 * 修改金币
	 *
	 * @param gold
	 * @param reason
	 *
	 * Role>
	 * 2017年9月25日 下午5:23:41
	 */
	public void changeGold(int gold, GlobalReason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.changeGold(this, gold, reason));
	}

	/**
	 * 同步金币
	 *
	 * @param reason
	 *
	 * Role>
	 * 2017年9月26日 上午10:42:24
	 */
	public void syncGold(GlobalReason reason) {
		ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.syncGold(this, reason));
	}

	public void saveToRedis(String propertiesName) {
		if (id < 1) {
			throw new RuntimeException(String.format("角色ID %d 异常", id));
		}
		String key = BydrKey.Role_Map.getKey(id);
		Method method = WRITEMETHODS.get(propertiesName);
		if (method != null) {
			try {
				Object value = method.invoke(this);
				if (value != null) {
					JedisManager.getJedisCluster().hset(key, propertiesName, value.toString());
				} else {
					LOGGER.warn("属性{}值为null", propertiesName);
				}

			} catch (Exception e) {
				LOGGER.error("属性存储", e);
			}
		} else {
			LOGGER.warn("属性：{}未找到对应方法", propertiesName);
		}
	}

}