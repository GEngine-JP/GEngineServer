package info.xiaomo.core.config;

import info.xiaomo.core.config.xml.ConfigDataXmlParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigCacheManager {

    private final Map<Class<?>, IConfigCache> caches = new HashMap<>();

    private static final ConfigCacheManager INSTANCE = new ConfigCacheManager();

    public static ConfigCacheManager getInstance() {
        return INSTANCE;
    }

    public void init(String path) {
        try {

            List<IConfigCache> list = ConfigDataXmlParser.parseCache(path);
            for (IConfigCache cache : list) {
                cache.build();
                caches.put(cache.getClass(), cache);
            }
        } catch (Exception e) {
            throw new RuntimeException("全局缓存初始化失败", e);
        }
    }

    /**
     * 初始化缓存，只初始化指定Class的缓存
     *
     * @param path      path
     * @param cacheList cacheList
     */
    public void init(String path, List<Class<?>> cacheList) {

        try {
            List<IConfigCache> list = ConfigDataXmlParser.parseCache(path);
            for (IConfigCache cache : list) {
                if (cacheList.contains(cache.getClass())) {
                    cache.build();
                    caches.put(cache.getClass(), cache);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("全局缓存初始化失败", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends IConfigCache> T getCache(Class<T> clazz) {
        return (T) caches.get(clazz);
    }

    /**
     * 重新构建
     */
    public void reBuild() {
        for (IConfigCache cache : caches.values()) {
            cache.build();
            caches.put(cache.getClass(), cache);
        }
    }

    /**
     * 重新构建
     *
     * @param cacheList cacheList
     */
    public void reBuild(List<Class<?>> cacheList) {
        for (IConfigCache cache : caches.values()) {
            if (cacheList.contains(cache.getClass())) {
                cache.build();
                caches.put(cache.getClass(), cache);
            }
        }
    }

    /**
     * 重新构建
     *
     * @param cacheClazz cacheClazz
     * @return boolean
     */
    public boolean reBuild(Class<?> cacheClazz) {
        for (IConfigCache cache : caches.values()) {
            if (cacheClazz.equals(cache.getClass())) {
                cache.build();
                caches.put(cache.getClass(), cache);
                return true;
            }
        }
        return false;
    }

}
