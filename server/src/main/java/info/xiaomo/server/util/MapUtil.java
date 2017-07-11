package com.sh.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MapUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);

	public static long getMapKey(long mapId, long line) {
		return line << 32 | mapId;
	}

	public static byte[] readMapBytes(String binaryDataFile) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(binaryDataFile);
			bos = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = fis.read(bytes);
			while (length != -1) {
				bos.write(bytes, 0, length);
				length = fis.read(bytes);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			LOGGER.error("读取地图的二进制文件发生错误。", e);
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
