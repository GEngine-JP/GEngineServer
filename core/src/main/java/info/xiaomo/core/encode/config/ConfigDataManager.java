package info.xiaomo.core.encode.config;

import info.xiaomo.core.encode.config.xml.ConfigDataXmlParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 把今天最好的表现当作明天最新的起点．．～
 * いま 最高の表現 として 明日最新の始発．．～
 * Today the best performance  as tomorrow newest starter!
 * Created by IntelliJ IDEA.
 * <p>
 * author: xiaomo
 * github: https://github.com/xiaomoinfo
 * email : xiaomo@xiaomo.info
 * QQ    : 83387856
 * Date  : 17/7/10 13:20
 * desc  :
 * Copyright(©) 2017 by xiaomo.
 */
public class ConfigDataManager {
    private static ConfigDataManager INSTANCE = new ConfigDataManager();


    private Map<Class<?>, ConfigDataContainer<?>> configContainers = new HashMap<>();

    public static ConfigDataManager getInstance() {
        return INSTANCE;
    }

    private ConfigDataManager() {
    }

    public void init(String path, List<Class<?>> configList, List<Class<?>> cacheList) {
        String xmlPath = "data_config.xml";
        try {
            List<ConfigDataContainer<?>> configDatas = ConfigDataXmlParser.parse(xmlPath);
            for (ConfigDataContainer<?> container : configDatas) {
                if(configList.contains(container.getClazz())){
                    container.load(path);
                    configContainers.put(container.getClazz(), container);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //全局缓存
        ConfigCacheManager.getInstance().init(xmlPath, cacheList);
    }

    @SuppressWarnings("unchecked")
    public <T extends IConfigData> T getByIdAndCacheName(Class<T> clazz, String cacheName, Object id) {
        ConfigDataContainer<T> container = (ConfigDataContainer) this.configContainers.get(clazz);
        return container == null ? null : container.getByIdAndCacheName(cacheName, id);
    }

    public <T extends IConfigData> T getById(Class<T> clazz, Object id) {
        return this.getByIdAndCacheName(clazz, ConfigDataContainer.DEFAULT_CACHE, id);
    }


}
