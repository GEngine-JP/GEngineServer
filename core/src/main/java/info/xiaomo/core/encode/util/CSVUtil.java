package info.xiaomo.core.encode.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

	public static CSVData readConfigDataFromUrl(String str, int skipLine) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new URL(str).openStream()));
			String tempString = null;
			List<String> lines = new ArrayList<String>();
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				lines.add(tempString);
			}
			reader.close();
			return new CSVData(lines, skipLine);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e1) {
			}
		}
	}

	public static CSVData read(String filepath, int skipLine) {
		BufferedReader br = null;
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));

			String line = null;
			ArrayList<String> lines = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			return new CSVData(lines, skipLine);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
