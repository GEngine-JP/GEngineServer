package info.xiaomo.server.cache;

import info.xiaomo.gameCore.persist.jdbc.JdbcTemplate;
import info.xiaomo.gameCore.persist.persist.*;
import info.xiaomo.server.server.GameContext;
import info.xiaomo.server.util.DruidDBPoolManager;
import info.xiaomo.server.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据管理类，主要是指玩家游戏数据，保存方式为二进制
 *
 * @author 张力
 */
public class CacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

    /**
     * 线程池线程个数
     */
    private static final int EXECUTOR_CORE_POOL_SIZE = 8;

    public static CacheManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final CacheManager INSTANCE = new CacheManager();
    }

    private PersistableCache cache;

    /**
     * 持久化任务map
     */
    private Map<String, PersistTask> persistTaskMap;

    /**
     * 持久化线程池
     */
    ScheduledThreadPoolExecutor executor;

    private CacheManager() {
    }

    public void init() {
        JdbcTemplate template = DruidDBPoolManager.get(GameContext.getOption().getServerId());
        this.cache = new PersistableCache(template, 0);

        executor = new ScheduledThreadPoolExecutor(EXECUTOR_CORE_POOL_SIZE, new ThreadFactory() {
            final AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                int curCount = count.incrementAndGet();
                return new Thread(r, "数据库持久化线程（线程池）-" + curCount);
            }
        });

        persistTaskMap = new HashMap<>();

    }

    /**
     * 注册一个持久化任务
     *
     * @param persistFactory
     */
    public void registerPersistTask(PersistFactory persistFactory) {
        for (Entry<Integer, JdbcTemplate> entry : DruidDBPoolManager.getPools().entrySet()) {
            PersistTask task = new PersistTask(entry.getValue(), persistFactory, cache);
            persistTaskMap.put(combineTableNameAndServerId(persistFactory.dataType(), entry.getKey()), task);
            executor.scheduleAtFixedRate(new WrapTask(task), 10 * 1000,
                    persistFactory.taskPeriod(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 持久化一条数据
     *
     * @param data
     * @param type
     */
    public void persist(Persistable data, PersistType type) {
        PersistTask task = persistTaskMap.get(combineTableNameAndServerId(data.dataType(), data.getId()));
        task.add(data, type);
    }

    /**
     * 当前数据数量
     *
     * @return
     */
    public int size() {
        return cache.size();
    }

    /**
     * 结束持久化任何，并且存储尚未更新的数据
     */
    public void store() {
        LOGGER.info("store() begin ...");
        try {
            LOGGER.info("persistTaskMap size()={}", persistTaskMap.size());
            int no = 0;
            for (PersistTask task : persistTaskMap.values()) {
                LOGGER.info("task={}", ++no);
                task.run();
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        LOGGER.info("store() end ...");
    }

    /**
     * 从缓存中获取一条数据
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(long id) {
        return (T) cache.get(id);
    }

    /**
     * 放入缓存数据
     *
     * @param obj
     */
    public void put(Persistable obj) {
        cache.put(obj);
    }

    /**
     * 保存一条数据
     *
     * @param dataId
     */
    public void update(long dataId) {
        Persistable obj = cache.get(dataId);
        if (obj == null) {
            LOGGER.error("Update not in cache, id={}, stack={}", dataId, Utils.getStackTrace());
            return;
        }
        persist(obj, PersistType.UPDATE);
    }

    /**
     * 立即保存指定id的数据
     *
     * @param dataId
     */
    public void updateImmediately(long dataId) {
        // 实时写入数据库
        Persistable obj = cache.get(dataId);
        if (obj == null) {
            LOGGER.error("Update not in cache, id={}, stack={}", dataId, Utils.getStackTrace());
            return;
        }
        PersistFactory factory = getFactory(obj);
        if (factory == null) {
            return;
        }
        try {
            JdbcTemplate template = DruidDBPoolManager.get(obj.getServerId());
            template.update(factory.createUpdateSql(), factory.createUpdateParameters(obj));
        } catch (Exception e) {
            LOGGER.error("立即更新数据库失败,id:" + obj.getId(), e);
        }
    }

    /**
     * 插入一条数据.<br/>
     * 该数据会添加进缓存，并且存入数据库.<br/>
     * 注意：该方法是异步写入数据库.<br/>
     */
    public void insert(Persistable obj) {
        cache.put(obj);
        // 添加到写队列中
        persist(obj, PersistType.INSERT);
    }

    /**
     * 立即插入一条数据.<br/>
     * 该数据会添加进缓存，并且延时存入数据库.<br/>
     */
    public void insertImmediately(Persistable obj) {
        if (obj == null) {
            return;
        }

        PersistFactory factory = getFactory(obj);
        if (factory == null) {
            return;
        }
        // 实时写入数据库
        try {
            JdbcTemplate template = DruidDBPoolManager.get(obj.getServerId());
            template.update(factory.createInsertSql(), factory.createInsertParameters(obj));
        } catch (Exception e) {
            LOGGER.error("立即写入数据库失败,id:" + obj.getId(), e);
        }
        cache.put(obj);
    }

    /**
     * 从缓存中移除数据
     *
     * @param id
     * @return
     */
    public Persistable remove(long id) {
        return cache.remove(id);
    }

    /**
     * 从缓存中移除数据，并且延时从数据库中删除
     *
     * @param id
     * @return
     */
    public Persistable removeFromDB(long id) {
        Persistable obj = cache.get(id);
        if (obj == null) {
            return null;
        }
        persist(obj, PersistType.DELETE);
        return obj;
    }

    /**
     * 从缓存中移除数据，并且立即从数据库中删除
     *
     * @param id
     * @return
     */
    public void removeFromDBImmediately(long id) {
        Persistable obj = cache.get(id);
        if (obj == null) {
            return;
        }
        PersistFactory factory = getFactory(obj);
        if (factory == null) {
            return;
        }
        try {
            JdbcTemplate template = DruidDBPoolManager.get(obj.getServerId());
            template.update(factory.createDeleteSql(), factory.createDeleteParameters(obj));
        } catch (Exception e) {
            LOGGER.error("立即删除数据库失败,id:" + obj.getId(), e);
        }
    }

    public PersistFactory getFactory(Persistable obj) {
        PersistTask task = persistTaskMap.get(combineTableNameAndServerId(obj.getServerId(), obj.getId()));
        if (task == null) {
            return null;
        }
        return task.getPersistFactory();
    }


    /**
     * 拼接表名和serverId
     *
     * @param dataType
     * @param serverId
     * @return
     */
    private static String combineTableNameAndServerId(int dataType, long serverId) {
        if (serverId == 0) {
            serverId = GameContext.getOption().getServerId();
        }
        return dataType + String.valueOf(serverId);
    }

}
