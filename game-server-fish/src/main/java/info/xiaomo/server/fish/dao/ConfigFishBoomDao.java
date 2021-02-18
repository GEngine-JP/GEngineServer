package info.xiaomo.server.fish.dao;

import java.util.List;
import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.shared.entity.ConfigFishBoom;
import org.mongodb.morphia.dao.BasicDAO;

public class ConfigFishBoomDao extends BasicDAO<ConfigFishBoom, Integer> {
	private static volatile ConfigFishBoomDao configFishBoomDao;

	public ConfigFishBoomDao(AbsMongoManager mongoManager) {
		super(ConfigFishBoom.class, mongoManager.getMongoClient(), mongoManager.getMorphia(), mongoManager.getMongoConfig().getDbName());
	}

	public static ConfigFishBoomDao getDB(AbsMongoManager mongoManager) {
		if (configFishBoomDao == null) {
			synchronized (ConfigFishBoomDao.class) {
				if (configFishBoomDao == null) {
					configFishBoomDao = new ConfigFishBoomDao(mongoManager);
				}
			}
		}
		return configFishBoomDao;
	}

	public static List<ConfigFishBoom> getAll() {
		return configFishBoomDao.createQuery().asList();
	}

}