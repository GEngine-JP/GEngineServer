package info.xiaomo.server.rpg.db;

import info.xiaomo.gengine.persist.mysql.persist.CacheAble;
import info.xiaomo.server.rpg.entify.User;
import info.xiaomo.server.rpg.server.ServerOption;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * @author : xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 2017/7/13 17:18
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class DataCenter {

    private static IDataProvider provider;

    public static void init(ServerOption option) throws Exception {
        provider = new MysqlDataProviderProxy(option);
    }

    /**
     * 更新数据到磁盘中
     *
     * @param immediately 是否立即更新
     */
    public static void updateData(CacheAble cache, boolean immediately) {
        provider.updateData(cache, immediately);
    }

    public static void updateData(CacheAble cache) {
        provider.updateData(cache, false);
    }

    public static void updateData(long dataId, int dataType, boolean immediately) {
        provider.updateData(dataId, dataType, immediately);
    }

    /**
     * 从磁盘和缓存中删除一条数据
     *
     * @param immediately 是否立即删除
     */
    public static void deleteData(CacheAble cache, boolean immediately) {
        provider.deleteData(cache, immediately);
    }

    /**
     * 新增一条数据到缓存和磁盘中
     *
     * @param immediately
     */
    public static void insertData(CacheAble cache, boolean immediately) {
        provider.insertData(cache, immediately);
    }

    /**
     * 添加一条数据到缓存中
     */
    public static void addData(CacheAble cache) {
        provider.addData(cache);
    }

    /**
     * 从缓存中移除一条数据
     */
    public static void removeData(CacheAble cache) {
        provider.removeData(cache);
    }

    public static User getUser(String loginName, int sid, int pid) {
        return provider.getUser(loginName, sid, pid);
    }

    /**
     * 全部保存
     */
    public static void store() {
        provider.store();
    }

    /**
     * 获取用户数据
     *
     * @param id
     * @return
     */
    public static User getUser(long id) {
        return provider.getUser(id);
    }

    /**
     * 获取用户数据
     *
     * @param loginName
     * @return
     */
    public static User getUser(String loginName) {
        return provider.getUser(loginName);
    }

    public static void registerUser(User user) {
        provider.registerUser(user);
    }
}
