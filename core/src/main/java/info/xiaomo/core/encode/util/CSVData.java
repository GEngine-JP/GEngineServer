package info.xiaomo.core.encode.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVData {

	public String[] tableHead;

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
		tableHead = line.trim().split(",");
	}

	public void readTR(List<String> lines) {
		// 读数据
		tableRows = new ArrayList<>();
		String line = null;
		String lineArray[] = null;
		for (int i = 0; i < lines.size(); i++) {
			Map<String, String> tr = new HashMap<String, String>();
			line = lines.get(i);
			lineArray = line.split(",");
			for (int j = 0; j < lineArray.length; j++) {
				if(j >= tableHead.length) {
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
		return "ListConfigData:" + this.tableHead.toString() + "=>" + tableRows.toString();
	}
}
