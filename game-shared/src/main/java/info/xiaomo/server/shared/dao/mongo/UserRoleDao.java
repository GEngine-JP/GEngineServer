/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.server.shared.dao.mongo;

import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.server.shared.entity.UserRole;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * 角色
 */
public class UserRoleDao extends BasicDAO<UserRole, Long> {

	private static volatile UserRoleDao userRoleDao;

	private UserRoleDao(AbsMongoManager mongoManager) {
		super(UserRole.class, mongoManager.getMongoClient(), mongoManager.getMorphia(),
				mongoManager.getMongoConfig().getDbName());
	}

	public static UserRoleDao getDB(AbsMongoManager mongoManager) {
		if (userRoleDao == null) {
			synchronized (UserRoleDao.class) {
				if (userRoleDao == null) {
					userRoleDao = new UserRoleDao(mongoManager);
				}
			}
		}
		return userRoleDao;
	}

	public static UserRole getRoleByUserId(long userId) {
		return userRoleDao.createQuery().filter("userId", userId).get();
	}

	public static void saveRole(UserRole UserRole) {
		userRoleDao.save(UserRole);
	}

}
