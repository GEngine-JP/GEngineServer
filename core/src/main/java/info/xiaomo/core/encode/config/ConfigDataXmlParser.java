package info.xiaomo.core.encode.config;

import info.xiaomo.core.encode.util.FileLoaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class ConfigDataXmlParser {

    private static final String CONFIG = "config";
    private static final String CLAZZ = "class";
    private static final String FILE = "file";
    private static final String KEY = "key";
    private static final String CONVERT = "convert";
    private static final String CONVERTER = "converter";
    private static final String FIELD = "field";

    private static final String CACHES = "caches";
    private static final String MAP = "map";
    private static final String CONFIG_CACHES = "configCaches";
    private static final String CONFIG_CACHE = "configCache";
    private static final String CONFIG_DATA = "configData";
    private static final String INFO_XIAOMO_CORE_CONFIG_I_CONFIG_CACHE = "info.xiaomo.core.config.IConfigCache";


    /**
     * 转换缓存
     *
     * @param path path
     * @return List<IConfigCache>
     * @throws DocumentException      DocumentException
     * @throws FileNotFoundException  FileNotFoundException
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws InstantiationException InstantiationException
     * @throws IllegalAccessException IllegalAccessException
     */
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
                    if (clazz.getInterfaces()[i].getName().equals(INFO_XIAOMO_CORE_CONFIG_I_CONFIG_CACHE)) {
                        IConfigCache cache = (IConfigCache) clazz.newInstance();
                        ret.add(cache);
                    }
                }
            }
        }
        return ret;
    }


    /**
     * 转换器
     *
     * @param config config
     * @return Map<String, IConverter>
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws InstantiationException InstantiationException
     * @throws IllegalAccessException IllegalAccessException
     */
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


    /**
     * 转换缓存
     *
     * @param config config
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    private static List<String> parseCaches(Element config) {
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

    /**
     * 转换
     */
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

                List<String> cacheList = parseCaches(config);

                Class<?> clazz = Class.forName(className);

                ConfigDataContainer<?> container = new ConfigDataContainer(
                        clazz, file, key, converterMap, cacheList,
                        globalConverter);
                ret.add(container);
            }
        }
        return ret;
    }


    /**
     * parseGlobalConverter
     *
     * @param config config
     * @return IConverter
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws InstantiationException InstantiationException
     * @throws IllegalAccessException IllegalAccessException
     */
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


    /**
     * @param path     path
     * @param fileName fileName
     * @return Class
     * @throws DocumentException      DocumentException
     * @throws FileNotFoundException  FileNotFoundException
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws InstantiationException InstantiationException
     * @throws IllegalAccessException IllegalAccessException
     */
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
