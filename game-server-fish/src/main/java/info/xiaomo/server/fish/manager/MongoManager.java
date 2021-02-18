package info.xiaomo.server.fish.manager;

import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.fish.dao.CFishDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mongodb
 * <p>
 * Role>
 * 2017年6月28日 下午3:33:14
 */
public class MongoManager extends AbsMongoManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
	private static final MongoManager INSTANCE_MANAGER = new MongoManager();

	public static MongoManager getInstance() {
		return INSTANCE_MANAGER;
	}

	@Override
	protected void initDao() {
		CFishDao.getDB(INSTANCE_MANAGER);

	}

}
