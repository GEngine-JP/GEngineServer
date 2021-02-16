/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.server.gate.manager;

import info.xiaomo.gengine.persist.mongo.AbsMongoManager;
import info.xiaomo.gengine.persist.mongo.dao.HallInfoDao;
import info.xiaomo.gengine.persist.mongo.dao.RoleDao;
import info.xiaomo.gengine.persist.mongo.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mongodb
 *
 *
 *
 */
public class MongoManager extends AbsMongoManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoManager.class);
    private static final MongoManager INSTANCE_MANAGER = new MongoManager();

    private MongoManager() {

    }

    public static MongoManager getInstance() {
        return INSTANCE_MANAGER;
    }

    @Override
    protected void initDao() {
        HallInfoDao.getDB(INSTANCE_MANAGER);
        UserDao.getDB(INSTANCE_MANAGER);
        RoleDao.getDB(INSTANCE_MANAGER);
    }

}
