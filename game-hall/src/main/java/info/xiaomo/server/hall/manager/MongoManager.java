package info.xiaomo.server.hall.manager;

import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.gengine.persist.mongo.dao.HallInfoDao;
import info.xiaomo.gengine.persist.mongo.dao.MailDao;
import info.xiaomo.gengine.persist.mongo.dao.RoleDao;
import info.xiaomo.gengine.persist.mongo.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mongodb
 * 
 *
 *  2017年6月28日 下午3:33:14
 */
public class MongoManager extends AbsMongoManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
	private static final MongoManager INSTANCE_MANAGER = new MongoManager();

	public static MongoManager getInstance() {
		return INSTANCE_MANAGER;
	}

	@Override
	protected void initDao() {
		HallInfoDao.getDB(INSTANCE_MANAGER);
		UserDao.getDB(INSTANCE_MANAGER);
		RoleDao.getDB(INSTANCE_MANAGER);
		MailDao.getDB(INSTANCE_MANAGER);
	}

}
