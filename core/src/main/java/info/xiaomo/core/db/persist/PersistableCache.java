package info.xiaomo.core.db.persist;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.EvictionListener;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import info.xiaomo.core.db.jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 数据库中的数据缓存
 * @author 张力
 */
public class PersistableCache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersistableCache.class);

	/**
	 * 数据map
	 */
	private Map<Long, Persistable> dataMap;


	public PersistableCache(JdbcTemplate template, int size) {
		dataMap = new ConcurrentLinkedHashMap.Builder<Long, Persistable>().maximumWeightedCapacity(size).weigher(Weighers.singleton())
				.listener(new EvictionListener<Long, Persistable>() {
					@Override
					public void onEviction(Long id, Persistable data) {
						if (data.isDirty()) {
							//如果发现数据是脏的，那么重新put一次，保证及时入库
							LOGGER.error("脏数据从缓存中移除了:" + id);
						}
					}
				}).build();
	}
	
	
	/**
	 * 当前数据数量
	 * @return
	 */
	public int size() {
		return dataMap.size();
	}


	/**
	 * 从缓存中获取一条数据
	 * @param dataid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(long id) {
		return (T) dataMap.get(id);
	}

	/**
	 * 放入缓存数据
	 * @param obj
	 */
	public void put(Persistable obj) {
		dataMap.put(obj.getId(), obj);
	}
	
	
	/**
	 * 从缓存中移除数据
	 * @param dataid
	 * @return
	 */
	public Persistable remove(long id) {
		return dataMap.remove(id);
	}

}
