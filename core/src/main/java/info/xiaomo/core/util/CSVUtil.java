package info.xiaomo.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class CSVUtil {

    /**
     * 网络地址
     * @param str str
     * @param skipLine skipLine
     * @return CSVData
     */
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
			} catch (IOException ignored) {
			}
		}
	}

    /**
     * 本地文件
     * @param filepath filepath
     * @param skipLine skipLine
     * @return CSVData
     */
	public static CSVData read(String filepath, int skipLine) {
		BufferedReader br = null;
		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));

			String line;
			ArrayList<String> lines = new ArrayList<>();
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

	public static class CSVData {

		private String[] tableHead;

		public List<Map<String, String>> tableRows;

		private int skipLine;


		public CSVData(List<String> lines, int skipLine) {

			this.skipLine = skipLine;

			// 跳过其他注释行
			skip(lines);

			// 读表头
			readTH(lines);

			// 读表行
			readTR(lines);
		}


		public void skip(List<String> lines){
			for(int i = 0; i <  skipLine; i++) {
				if(lines.isEmpty()){
					break;
				}
				lines.remove(0);
			}
		}

		public void readTH(List<String> lines) {
			String line = lines.remove(0);
			tableHead = line.trim().split(Symbol.DOUHAO);
		}

		public void readTR(List<String> lines) {
			// 读数据
			tableRows = new ArrayList<>();
			String line;
			String lineArray[];
			for (String line1 : lines) {
				Map<String, String> tr = new HashMap<>();
				line = line1;
				lineArray = line.split(Symbol.DOUHAO);
				for (int j = 0; j < lineArray.length; j++) {
					if (j >= tableHead.length) {
						continue;
					}
					String col = lineArray[j];
					tr.put(tableHead[j], col);
				}
				tableRows.add(tr);
			}
		}

		@Override
		public String toString() {
			return "ListConfigData:" + Arrays.toString(this.tableHead) + "=>" + tableRows.toString();
		}
	}
}
