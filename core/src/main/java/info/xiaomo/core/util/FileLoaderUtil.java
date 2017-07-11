package info.xiaomo.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileLoaderUtil {

    /**
     * 通过文件名找URL
     * @param fileName 文件名
     * @return URL
     */
	public static URL findURLByFileName(String fileName) {
		ClassLoader cl = FileLoaderUtil.class.getClassLoader();
		return cl.getResource(fileName);
	}

	/**
	 * 通过文件名找输入流
	 * @param fileName 文件名
	 * @return 输入流
	 */
	public static InputStream findInputStreamByFileName(String fileName) {
		try {
			URL url = findURLByFileName(fileName);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setUseCaches(false);
			return urlConnection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

