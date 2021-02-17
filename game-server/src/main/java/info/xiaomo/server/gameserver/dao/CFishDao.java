package info.xiaomo.server.gameserver.dao;

import java.util.List;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.gameserver.entity.CFish;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 角色
 *
 *
 *  2017-02-27
 */
public class CFishDao extends BasicDAO<CFish, Integer> {

	private static volatile CFishDao cFishDao;

	public CFishDao(AbsMongoManager mongoManager) {
		super(CFish.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),
				mongoManager.getMongoConfig().getDbName());
	}

	public static CFishDao getDB(AbsMongoManager mongoManager) {
		if (cFishDao == null) {
			synchronized (CFishDao.class) {
				if (cFishDao == null) {
					cFishDao = new CFishDao(mongoManager);
					// cFishDao.getDs().ensureIndexes(true);
				}
			}
		}
		return cFishDao;
	}

	public static List<CFish> getAll() {
		return cFishDao.createQuery().asList();
	}

}
