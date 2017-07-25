package info.xiaomo.server.db;


import info.xiaomo.core.db.jdbc.ConnectionPool;
import info.xiaomo.core.db.jdbc.DruidConnectionPool;
import info.xiaomo.core.db.jdbc.JdbcTemplate;
import info.xiaomo.core.db.persist.CacheAble;
import info.xiaomo.core.db.persist.Persistable;
import info.xiaomo.server.db.mapper.UserMapper;
import info.xiaomo.server.db.msyql.UserPersistFactory;
import info.xiaomo.server.entify.User;
import info.xiaomo.server.server.ServerOption;
import info.xiaomo.server.system.user.field.UserField;
import info.xiaomo.server.util.JdbcUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mysql的数据库代理,所有的数据存储相关的请求,都代理到了CacheManager
 *
 * @author Administrator
 */
public class MysqlDataProviderProxy implements IDataProvider {

    private static final Object NULL = new Object();
    private static final String SELECT_USER = "select " +
            "id," +
            " login_name," +
            " server_id," +
            " platform_id," +
            " gm_level," +
            " id_number," +
            " register_time" +
            " from p_user";

    private Map<String, Long> nameSidPid2Uid = new ConcurrentHashMap<>();

    private Map<Long, Object> uidMap = new ConcurrentHashMap<>();

    private JdbcTemplate template;

    private MysqlDataProvider provider;


    /**
     * 是否有这个玩家
     *
     * @param id id
     * @return bool
     */
    private boolean hasUser(long id) {

        return uidMap.containsKey(id);
    }

    MysqlDataProviderProxy(ServerOption option) throws Exception {
        // 创建数据库模板
        ConnectionPool connectionPool = new DruidConnectionPool(option.getGameDbConfigPath());
        JdbcTemplate template = new JdbcTemplate(connectionPool);
        this.template = template;
        provider = new MysqlDataProvider();
        provider.init(this.template);
        JdbcUtil.init(this.template);

        //注册factory
        provider.registerPersistTask(new UserPersistFactory());

        //加载所有的uid
        List<Map<String, Object>> users = template.queryList(SELECT_USER, JdbcTemplate.MAP_MAPPER);
        for (Map<String, Object> user : users) {
            long id = (long) user.get(UserField.ID);
            String loginName = (String) user.get(UserField.LOGIN_NAME);
            int sid = (int) user.get(UserField.SERVER_ID);
            int pid = (int) user.get(UserField.PLATFORM_ID);
            nameSidPid2Uid.put(loginName + "_" + sid + "_" + pid, id);
            uidMap.put(id, NULL);
        }

    }

    @Override
    public void updateData(CacheAble cache, boolean immediately) {
        provider.update(cache.getId(), cache.dataType(), immediately);

    }

    @Override
    public void updateData(long dataId, int dataType, boolean immediately) {
        provider.update(dataId, dataType, immediately);
    }

    @Override
    public void deleteData(CacheAble cache, boolean immediately) {
        provider.removeFromDisk(cache.getId(), cache.dataType(), immediately);
    }

    @Override
    public void insertData(CacheAble cache, boolean immediately) {
        provider.insert((Persistable) cache, immediately);
    }

    @Override
    public void addData(CacheAble cache) {
        provider.put((Persistable) cache);
    }

    @Override
    public void removeData(CacheAble cache) {
        provider.removeFromCache(cache.getId(), cache.dataType());
    }

    @Override
    public void registerUser(User user) {
        nameSidPid2Uid.put(user.getLoginName() + "_" + user.getServerId() + "_" + user.getPlatformId(), user.getId());
        uidMap.put(user.getId(), NULL);
    }

    public User getUser(String loginName, int sid, int pid) {
        String key = loginName + "_" + sid + "_" + pid;
        Long id = nameSidPid2Uid.get(key);
        if (id == null) {
            return null;
        }
        User user = provider.get(id, DbDataType.USER);
        if (user == null) {
            // 从数据库中查询
            user = this.template.query("select id, loginName, sid, pid, client, type, IDNumber, regTime from p_user " +
                            "where id = ?",
                    new UserMapper(), id);
            return user;
        }
        return user;
    }

    /**
     * 获取用户数据
     *
     * @param id id
     * @return User
     */
    public User getUser(long id) {

        if (hasUser(id)) {
            User user = provider.get(id, DbDataType.USER);
            if (user == null) {
                // 从数据库中查询
                user = this.template.query(SELECT_USER, new UserMapper(), id);
                if (user != null) {
                    provider.put(user);
                }
                return user;
            }
        }
        return null;
    }

    @Override
    public User getUser(String loginName) {
        return template.query("select * from p_user where loginName = ?", new UserMapper(), loginName);
    }

    @Override
    public void store() {
        provider.store();
    }


}
