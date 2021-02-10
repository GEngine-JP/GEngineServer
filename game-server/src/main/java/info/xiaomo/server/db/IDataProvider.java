package info.xiaomo.server.db;

import info.xiaomo.core.persist.mysql.persist.CacheAble;
import info.xiaomo.server.entify.User;

/**
 * 数据提供者
 *
 * @author 小莫
 * 2017年7月6日 下午9:30:03
 */
public interface IDataProvider {

    /**
     * 更新数据到磁盘中
     * @param cache cache
     * @param immediately 是否立即更新
     */
    void updateData(CacheAble cache, boolean immediately);

    /**
     * 更新数据
     *
     * @param dataId      dataId
     * @param dataType    dataType
     * @param immediately immediately
     */
    void updateData(long dataId, int dataType, boolean immediately);

    /**
     * 从磁盘和缓存中删除一条数据
     * @param cache cache
     * @param immediately 是否立即删除
     */
    void deleteData(CacheAble cache, boolean immediately);

    /**
     * 新增一条数据到缓存和磁盘中
     *
     * @param immediately 立即
     * @param cache cache
     */
    void insertData(CacheAble cache, boolean immediately);

    /**
     * 添加一条数据到缓存中
     * * @param cache cache
     */
    void addData(CacheAble cache);

    /**
     * 从缓存中移除一条数据
     *
     * @param cache cache
     */
    void removeData(CacheAble cache);

    /**
     * 获取用户数据
     *
     * @param loginName 用户登录名
     * @param sid       服务器id
     * @param pid       平台id
     * @return User
     */
    User getUser(String loginName, int sid, int pid);

    /**
     * 获取用户数据
     *
     * @param id id
     * @return User
     */
    User getUser(long id);

    /**
     * 获取玩家
     *
     * @param loginName loginName
     * @return User
     */
    User getUser(String loginName);


    /**
     * 注册用户
     *
     * @param user user
     */
    void registerUser(User user);

    /**
     * 存储
     */
    void store();


}
