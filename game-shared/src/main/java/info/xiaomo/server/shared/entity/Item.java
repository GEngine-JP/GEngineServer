package info.xiaomo.server.shared.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.Date;
import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.utils.JsonUtil;
import info.xiaomo.server.shared.rediskey.HallKey;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.redisson.api.RRemoteService;

/**
 * 道具 <br>
 * redis实时存储
 *
 * <p>暂时各个服务器各种修改，可以考虑再通过redisson {@link RRemoteService} 实现在大厅处理 2017年9月18日 下午2:31:29
 */
@JSONType(serialzeFeatures = SerializerFeature.WriteClassName)
@Entity(value = "item", noClassnameStored = true)
@Data
public class Item {

	@JSONField
	private long id;
	/**
	 * 配置Id
	 */
	@JSONField
	private int configId;
	/**
	 * 数量
	 */
	@JSONField
	private int num;
	/**
	 * 创建时间
	 */
	@JSONField
	private Date createTime;

	public Item() {
		id = Config.getId();
	}


	@Override
	public String toString() {
		return JsonUtil.toJSONString(this);
	}

	public void saveToRedis(long roleId) {
		String key = HallKey.Role_Map_Packet.getKey(roleId);
		JedisManager.getJedisCluster().hset(key, String.valueOf(id), JsonUtil.toJSONString(this));
	}
}
