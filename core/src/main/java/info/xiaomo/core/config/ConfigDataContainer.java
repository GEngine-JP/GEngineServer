package info.xiaomo.core.config;

import info.xiaomo.core.util.CSVUtil;
import info.xiaomo.core.util.Cast;
import info.xiaomo.core.util.ReflectUtil;
import info.xiaomo.core.util.ReflectUtil.PropertyDesc;
import info.xiaomo.core.util.Symbol;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
 * Date  : 17/7/10 13:23
 * desc  : 配置文件容器
 * Copyright(©) 2017 by xiaomo.
 */
public class ConfigDataContainer<T extends IConfigData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDataContainer.class);

    static final String DEFAULT_CACHE = "default";
    private static final String CSV_SUFFIX = ".csv";
    private static final int SKIP_LINE = 3;

    private Map<String, Map<Object, T>> mapCaches;

    private List<T> list;

    private static Set<String> errKey = new HashSet<>();

    /**
     * 该配置对应的Class
     */
    private Class<T> clazz;

    /**
     * 该配置对应的文件名
     */
    private String fileName;

    /**
     * 该配置的key
     */
    private String key;

    /**
     * 该配置其他需要做成Map形式的key
     */
    private List<String> extraKeyList;

    /**
     * 属性转换器
     */
    private Map<String, IConverter> converterMap;

    /**
     * 全局转换器类，直接转换整个配置文件
     */
    private IConverter globalConverter;


    public ConfigDataContainer(Class<T> clazz,
                               String fileName,
                               String key,
                               Map<String, IConverter> converterMap,
                               List<String> extraKeyList,
                               IConverter globalConverter) {
        this.clazz = clazz;
        this.fileName = fileName;
        this.key = key;
        this.extraKeyList = extraKeyList;
        this.converterMap = converterMap;
        this.globalConverter = globalConverter;
        // 这里不用ConcurrentHashMap的原因是配置表都是只读的，在Reload的时候，会有其他机制保证期线程安全性
    }

    /**
     * 通过id和缓存名字获取一个实体
     *
     * @param cacheName cacheName
     * @param key       key
     * @return T
     */
    public T getByIdAndCacheName(String cacheName, Object key) {
        Map<Object, T> cache = this.mapCaches.get(cacheName);
        return cache == null ? null : cache.get(key);
    }

    /**
     * 获取class
     *
     * @return Class
     */
    public Class<T> getClazz() {
        return clazz;
    }


    public List<T> getList() {
        return this.list;
    }

    /**
     * 加载后执行的操作
     */
    private void afterLoad() {
        for (IConfigData config : list) {
            config.afterLoad();
        }
    }

    /**
     * 加载数据
     *
     * @param filePath filePath
     * @throws InstantiationException    InstantiationException
     * @throws IllegalAccessException    IllegalAccessException
     * @throws IllegalArgumentException  IllegalArgumentException
     * @throws InvocationTargetException InvocationTargetException
     */
    public void load(String filePath) throws
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {


        String file = filePath + "/" + fileName + CSV_SUFFIX;
        CSVUtil.CSVData data = CSVUtil.read(file, SKIP_LINE);
        if (data == null) {
            return;
        }

        // 加载数据到list
        List<T> list = new ArrayList<>();
        for (Map<String, String> map : data.tableRows) {
            T config = converterObject(map);
            list.add(config);
        }

        Map<String, Map<Object, T>> mapCaches = new HashMap<>();

        // 生成默认key的缓存
        Map<Object, T> defaultCache = buildCache(list, this.key);
        mapCaches.put(DEFAULT_CACHE, defaultCache);

        // 处理额外的key缓存
        for (String key : extraKeyList) { // 遍历key
            Map<Object, T> cache = buildCache(list, key);
            mapCaches.put(key, cache);
        }

        this.mapCaches = mapCaches;
        this.list = list;

        // 回调afterLoad函数
        this.afterLoad();

        errKey.clear();

    }

    /**
     * converterObject
     *
     * @param data data
     * @return T
     * @throws InstantiationException    InstantiationException
     * @throws IllegalAccessException    IllegalAccessException
     * @throws IllegalArgumentException  IllegalArgumentException
     * @throws InvocationTargetException InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    private T converterObject(Map<String, String> data) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (this.globalConverter != null) {// 全局转换器
            return (T) globalConverter.convert(data);
        }

        T config = clazz.newInstance();
        boolean isNull = true;
        for (String value : data.values()) {
            if (value != null && !value.equals("")) {
                isNull = false;
                break;
            }
        }
        if (isNull) {
            LOGGER.error("配置表：" + fileName + "存在空数据，用EM检查一下，防止getList时候出BUG！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        }

        for (String key : data.keySet()) {
            String value = data.get(key);
            PropertyDesc desc = ReflectUtil.getPropertyDesc(clazz, key);
            if (desc == null) {
                if (errKey.contains(key)) {
                    continue;
                }
                LOGGER.error("配置表：" + fileName + "中的" + key + "字段在" + clazz.getName() + "没有找到setter和getter方法");
                errKey.add(key);
                continue;
            }

            try {
                Object newValue = covertValue(desc, value);
                Method method = desc.getWriteMethod();
                method.invoke(config, newValue);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }
        return config;
    }


    /**
     * buildCache
     *
     * @param list list
     * @param key  key
     * @return Map
     * @throws IllegalAccessException    IllegalAccessException
     * @throws IllegalArgumentException  IllegalArgumentException
     * @throws InvocationTargetException InvocationTargetException
     */
    private Map<Object, T> buildCache(List<T> list, String key) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        String[] keys = key.split(Symbol.JINHAO);
        ReflectUtil.PropertyDesc propertyDesc[] = new PropertyDesc[keys.length];

        for (int i = 0; i < keys.length; i++) {
            String keyField = keys[i];
            // 获取该key对应的方法
            PropertyDesc desc = ReflectUtil.getPropertyDesc(clazz, keyField);
            if (desc == null || desc.getReadMethod() == null) {// 没有这个字段
                LOGGER.error("配置表：" + fileName + "中的" + keyField + " 字段在" + clazz.getName()
                        + "没有找到setter和getter方法");
                return Collections.emptyMap();
            }
            propertyDesc[i] = desc;
        }

        Map<Object, T> cache = new HashMap<>();

        // 遍历所有的数据
        for (T t : list) {

            Object value = buildKey(t, propertyDesc);
            cache.put(value, t);
        }
        return cache;
    }

    /**
     * @param value        value
     * @param propertyDesc propertyDesc
     * @return Object
     * @throws IllegalAccessException    IllegalAccessException
     * @throws IllegalArgumentException  IllegalArgumentException
     * @throws InvocationTargetException InvocationTargetException
     */
    public Object buildKey(T value, ReflectUtil.PropertyDesc propertyDesc[]) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (propertyDesc.length == 1) {// 单个key
            return propertyDesc[0].getReadMethod().invoke(value);
        }
        StringBuilder key = new StringBuilder();
        for (PropertyDesc desc : propertyDesc) {
            Object v = desc.getReadMethod().invoke(value);
            key.append(v.toString());
            key.append(Symbol.JINHAO);
        }
        if (key.length() > 0) {
            key.deleteCharAt(key.length() - 1);
        }
        return key.toString();
    }


    /**
     * @param desc     desc
     * @param oldValue oldValue
     * @return Object
     */
    private Object covertValue(PropertyDesc desc, String oldValue) {
        Object value = oldValue;
        if (this.converterMap.containsKey(desc.getName())) {
            IConverter converter = this.converterMap.get(desc.getName());
            value = converter.convert(oldValue);
        } else if (desc.getPropertyType() == int.class || desc.getPropertyType() == Integer.class) {
            value = Cast.toInteger(oldValue);
        } else if (desc.getPropertyType() == long.class || desc.getPropertyType() == Long.class) {
            value = Cast.toLong(oldValue);
        } else if (desc.getPropertyType() == float.class || desc.getPropertyType() == Float.class) {
            if (StringUtils.isEmpty(oldValue))
                oldValue = "0.0";
            value = Float.parseFloat(oldValue);
        } else if (desc.getPropertyType() == double.class || desc.getPropertyType() == Double.class) {
            value = Cast.toDouble(oldValue);
        } else if (desc.getPropertyType() == int[].class) {
            if (StringUtils.isEmpty(oldValue)) {
                value = new int[0];
            } else {
                value = Cast.stringToInts(oldValue, Symbol.JINHAO);
            }
        } else if (desc.getPropertyType() == String[].class) {
            if (StringUtils.isEmpty(oldValue)) {
                value = new String[0];
            } else {
                value = oldValue.split(Symbol.JINHAO);
            }
        }
        return value;
    }
}
