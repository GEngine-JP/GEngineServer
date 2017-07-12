package info.xiaomo.core.config;

import info.xiaomo.core.config.xml.ConfigDataXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDataManager.class);

    private static ConfigDataManager INSTANCE = new ConfigDataManager();


    private Map<Class<?>, ConfigDataContainer<?>> configContainers = new HashMap<>();

    public static ConfigDataManager getInstance() {
        return INSTANCE;
    }

    private ConfigDataManager() {
    }


    public void init(String configDataPath) {
        String xmlPath = "data_config.xml";
        try {
            List<ConfigDataContainer<?>> configDatas = ConfigDataXmlParser.parse(xmlPath);
            LOGGER.warn("配置条数：" + configDatas.size());
            for (ConfigDataContainer<?> container : configDatas) {
                container.load(configDataPath);
                configContainers.put(container.getClazz(), container);
            }
        } catch (Exception e) {
            LOGGER.error("加载配置文件失败...",e);
            throw new RuntimeException(e);
        }
        //全局缓存
        ConfigCacheManager.getInstance().init(xmlPath);
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

    @SuppressWarnings("unchecked")
    public <T extends AbstractConfigData> List<T> getList(Class<T> clazz){
        ConfigDataContainer<T> configDataContainer = (ConfigDataContainer<T>) this.configContainers.get(clazz);
        if (configDataContainer == null) {
            return null;
        }
        return configDataContainer.getList();
    }


}
