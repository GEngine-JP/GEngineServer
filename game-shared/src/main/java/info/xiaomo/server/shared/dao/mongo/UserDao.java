/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.server.shared.dao.mongo;

import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.shared.entity.User;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

/**
 * 用戶
 */
public class UserDao extends BasicDAO<User, Long> {

	private static volatile UserDao userDao;

	private UserDao(AbsMongoManager mongoManager) {
		super(User.class, mongoManager.getMongoClient(), mongoManager.getMorphia(), mongoManager.getMongoConfig().getDbName());
	}

	public static UserDao getDB(AbsMongoManager mongoManager) {
		if (userDao == null) {
			synchronized (UserDao.class) {
				if (userDao == null) {
					userDao = new UserDao(mongoManager);
				}
			}
		}
		return userDao;
	}

	public static User findByAccount(String accunt) {
		Query<User> query = userDao.createQuery().filter("accunt", accunt);
		return query.get();
	}

	public static void saveUser(User User) {
		userDao.save(User);
	}

}
