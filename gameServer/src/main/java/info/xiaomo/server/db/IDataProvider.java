package info.xiaomo.server.db;

import info.xiaomo.core.db.persist.CacheAble;
import info.xiaomo.server.entify.User;

/**
 * 数据提供者
 *
 * @author 小莫
 *         2017年7月6日 下午9:30:03
 */
public interface IDataProvider {

    /**
     * 更新数据到磁盘中
     *
     * @param immediately 是否立即更新
     */
    void updateData(CacheAble cache, boolean immediately);

    void updateData(long dataId, int dataType, boolean immediately);

    /**
     * 从磁盘和缓存中删除一条数据
     *
     * @param immediately 是否立即删除
     */
    void deleteData(CacheAble cache, boolean immediately);

    /**
     * 新增一条数据到缓存和磁盘中
     *
     * @param immediately 立即
     */
    void insertData(CacheAble cache, boolean immediately);

    /**
     * 添加一条数据到缓存中
     */
    void addData(CacheAble cache);

    /**
     * 从缓存中移除一条数据
     */
    void removeData(CacheAble cache);

    /**
     * 获取用户数据
     *
     * @param loginName 用户登录名
     * @param sid       服务器id
     * @param pid       平台id
     * @return
     */
    User getUser(String loginName, int sid, int pid);

    /**
     * 获取用户数据
     *
     * @param id id
     * @return User
     */
    User getUser(long id);


    void registerUser(User user);

    void store();


}
