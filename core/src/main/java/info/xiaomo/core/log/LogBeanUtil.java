package info.xiaomo.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 日志对象获取函数
 * @author 张力
 * @date 2015-4-3 下午4:21:22
 *
 */
class LogBeanUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(LogBeanUtil.class);

	public static <T> Set<Class<T>> getSubClasses(String packageName, Class<T> parentClass) {
		Set<Class<T>> ret = new LinkedHashSet<>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		try {
			Enumeration<URL> dirs = LogBeanUtil.class.getClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					ret.addAll(findSubClazzFromFile(packageName, filePath, recursive, parentClass));
				} else if ("jar".equals(protocol)) {
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					ret.addAll(findSubClassFromJar(jar, packageName, packageDirName, parentClass, recursive));
				}
			}
		} catch (Throwable e) {
			LOGGER.error("读取日志Class文件出错", e);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<Class<T>> findSubClassFromJar(JarFile jar, String packageName, String packageDirName,
                                                        Class<T> parentClass, boolean recursive) {

		Set<Class<T>> ret = new LinkedHashSet<>();
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.charAt(0) == '/') {
				name = name.substring(1);
			}
			String packageNameNew = packageName;
			if (!name.startsWith(packageDirName)) {
				continue;
			}

			int idx = name.lastIndexOf('/');
			if (idx != -1) {
				packageNameNew = name.substring(0, idx).replace('/', '.');
			}
			if (idx != -1 || recursive) {
				if (name.endsWith(".class") && !entry.isDirectory()) {
					String className = name.substring(packageNameNew.length() + 1, name.length() - 6);
					if(!className.endsWith("Log")){
						continue;
					}
					try {
						Class<T> loadClass = (Class<T>) Class.forName(packageNameNew + '.' + className, false,
								LogBeanUtil.class.getClassLoader());
						if ((parentClass.isAssignableFrom(loadClass)) && (!parentClass.equals(loadClass))) {
							ret.add(loadClass);
						}
					} catch (Throwable e) {
						LOGGER.error("读取Jar中的Class文件出错", e);
					}
				}
			}
		}
		return ret;

	}

	@SuppressWarnings("unchecked")
	public static <T> Set<Class<T>> findSubClazzFromFile(String packageName, String packagePath,
                                                         final boolean recursive, Class<T> parentClass) {

		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return Collections.emptySet();
		}

		Set<Class<T>> ret = new LinkedHashSet<>();

		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || file.getName().endsWith(".class");
			}
		});

		for (File file : dirfiles) {
			if (file.isDirectory()) {
				ret.addAll(findSubClazzFromFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						parentClass));
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				if(!className.endsWith("Log")){
					continue;
				}
				try {
					Class<T> clazz = (Class<T>) Class.forName(packageName + '.' + className, false,
							LogBeanUtil.class.getClassLoader());
					if ((parentClass.isAssignableFrom(clazz)) && !parentClass.equals(clazz)) {
						ret.add(clazz);
					}
				} catch (Throwable e) {
					LOGGER.error("读取文件夹中的Class文件出错", e);
				}
			}
		}
		return ret;
	}

}
