package info.xiaomo.core.config.xml;

import info.xiaomo.core.config.ConfigDataContainer;
import info.xiaomo.core.config.IConfigCache;
import info.xiaomo.core.config.IConverter;
import info.xiaomo.core.util.FileLoaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
 * Date  : 17/7/10 13:41
 * desc  : xml文件读取
 * Copyright(©) 2017 by xiaomo.
 */
public class ConfigDataXmlParser {

    // 一条config的字段
    private static final String CONFIG = "config"; //单个文件
    private static final String CLAZZ = "class"; //对应的实体类名
    private static final String FILE = "file"; // csv文件名（不包含后缀)
    private static final String KEY = "key"; // 主键

    private static final String CONVERT = "convert";
    private static final String CONVERTER = "converter";
    private static final String FIELD = "field";
    private static final String CACHES = "caches";
    private static final String MAP = "map";
    private static final String CONFIG_CACHES = "configCaches";
    private static final String CONFIG_CACHE = "configCache";
    private static final String CONFIG_DATA = "configData";


    @SuppressWarnings("unchecked")
    public static List<ConfigDataContainer<?>> parse(String path)
            throws DocumentException, FileNotFoundException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = FileLoaderUtil.findInputStreamByFileName(path);
        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();

        List<ConfigDataContainer<?>> ret = new ArrayList<>();
        Iterator<Element> data = root.elementIterator(CONFIG_DATA);
        while (data.hasNext()) {
            Element configData = data.next();
            Iterator<Element> it = configData.elementIterator(CONFIG);
            while (it.hasNext()) {
                Element config = it.next();
                String className = config.attributeValue(CLAZZ);
                String file = config.attributeValue(FILE);
                String key = config.attributeValue(KEY);

                IConverter globalConverter = parseGlobalConverter(config);

                Map<String, IConverter> converterMap = parseConvert(config);

                List<String> cacheList = parseCache(config);

                Class<?> clazz = Class.forName(className);

                ConfigDataContainer<?> container = new ConfigDataContainer(
                        clazz, file, key, converterMap, cacheList,
                        globalConverter);
                ret.add(container);
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static List<IConfigCache> parseCache(String path)
            throws DocumentException, FileNotFoundException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = FileLoaderUtil.findInputStreamByFileName(path);
        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();

        List<IConfigCache> ret = new ArrayList<>();
        Iterator<Element> data = root.elementIterator(CONFIG_CACHES);
        while (data.hasNext()) {
            Element configCaches = data.next();
            Iterator<Element> it = configCaches.elementIterator(CONFIG_CACHE);
            while (it.hasNext()) {
                Element config = it.next();
                String className = config.attributeValue(CLAZZ);
                Class<?> clazz = Class.forName(className);
                int size = clazz.getInterfaces().length;
                for (int i = 0; i < size; i++) {
                    if (clazz.getInterfaces()[i].getName().equals(
                            "info.xiaomo.core.config.IConfigCache")) {
                        IConfigCache cache = (IConfigCache) clazz.newInstance();
                        ret.add(cache);
                    }
                }
            }
        }
        return ret;
    }


    @SuppressWarnings("unchecked")
    private static Map<String, IConverter> parseConvert(Element config)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Map<String, IConverter> converterMap = new HashMap<>();
        Iterator<Element> convertIt = config.elementIterator(CONVERT);
        while (convertIt.hasNext()) {
            Element convert = convertIt.next();
            String field = convert.attributeValue(FIELD);
            String converterClassName = convert.attributeValue(CONVERTER);
            Class<?> converterClass = Class.forName(converterClassName);
            IConverter converter = (IConverter) converterClass.newInstance();
            converterMap.put(field, converter);
        }
        return converterMap;
    }

    @SuppressWarnings("unchecked")
    private static List<String> parseCache(Element config) {
        List<String> ret = new ArrayList<>();
        Element caches = config.element(CACHES);
        if (caches == null) {
            return ret;
        }
        Iterator<Element> mapIt = caches.elementIterator(MAP);
        while (mapIt.hasNext()) {
            Element map = mapIt.next();
            String key = map.attributeValue(KEY);
            ret.add(key);
        }
        return ret;
    }

    private static IConverter parseGlobalConverter(Element config)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        String globalConverterClassName = config.attributeValue(CONVERTER);
        IConverter globalConverter = null;
        if (!StringUtils.isEmpty(globalConverterClassName)) {
            Class<?> globalConverterClass = Class
                    .forName(globalConverterClassName);
            globalConverter = (IConverter) globalConverterClass.newInstance();
        }
        return globalConverter;
    }

    @SuppressWarnings("unchecked")
    public static Class<?> getClazz(String path, String fileName)
            throws DocumentException, FileNotFoundException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = FileLoaderUtil.findInputStreamByFileName(path);
        Document document = saxReader.read(inputStream);
        Element root = document.getRootElement();
        Class<?> ret = null;
        Iterator<Element> data = root.elementIterator(CONFIG_DATA);
        while (data.hasNext()) {
            Element configData = data.next();
            Iterator<Element> it = configData.elementIterator(CONFIG);
            while (it.hasNext()) {
                Element config = it.next();
                String className = config.attributeValue(CLAZZ);
                String file = config.attributeValue(FILE);
                if (fileName.equals(file)) {
                    ret = Class.forName(className);
                    break;
                }
            }
        }
        return ret;
    }

}
