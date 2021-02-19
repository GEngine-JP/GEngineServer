package info.xiaomo.server.shared.dao.mongo;

import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.shared.entity.Guild;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 公会
 * <p>
 * 2017年9月22日 上午10:38:16
 */
public class GuildDao extends BasicDAO<Guild, Long> {
	private static volatile GuildDao guildDao;

	public GuildDao(AbsMongoManager mongoManager) {
		super(
				Guild.class,
				mongoManager.getMongoClient(),
				mongoManager.getMorphia(),
				mongoManager.getMongoConfig().getDbName());
	}

	public static GuildDao getDB(AbsMongoManager mongoManager) {
		if (guildDao == null) {
			synchronized (GuildDao.class) {
				if (guildDao == null) {
					guildDao = new GuildDao(mongoManager);
				}
			}
		}
		return guildDao;
	}

	/**
	 * 存储
	 * <p>
	 * 2017年9月22日 上午10:45:52
	 *
	 * @param Guild
	 */
	public static void saveGuild(Guild Guild) {
		guildDao.save(Guild);
	}
}
