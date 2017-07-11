package info.xiaomo.core.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TableCompareUtil {

	/**
	 * 属性变更规则
	 */
	private static Map<String, List<String>> FIELD_CHANGE_RULES = new HashMap<>();

	/**
	 * 初始化变更规则
	 */
	static {

		List<String> bigintlist = new ArrayList<>();
		bigintlist.add("varchar");
		bigintlist.add("longtext");
		bigintlist.add("text");
		bigintlist.add("bigint");
		FIELD_CHANGE_RULES.put("bigint", bigintlist);

		List<String> bitlist = new ArrayList<>();
		bitlist.add("longtext");
		bitlist.add("varchar");
		bitlist.add("text");
		bitlist.add("bigint");
		bitlist.add("integer");
		bitlist.add("int");
		bitlist.add("int unsigend");
		bitlist.add("bit");
		FIELD_CHANGE_RULES.put("bit", bitlist);

		List<String> intlist = new ArrayList<>();
		intlist.add("longtext");
		intlist.add("varchar");
		intlist.add("text");
		intlist.add("bigint");
		intlist.add("integer");
		intlist.add("int");
		intlist.add("int unsigned");
		FIELD_CHANGE_RULES.put("int", intlist);

		FIELD_CHANGE_RULES.put("integer", intlist);

		List<String> shortlist = new ArrayList<>();
		shortlist.add("longtext");
		shortlist.add("varchar");
		shortlist.add("text");
		shortlist.add("bigint");
		shortlist.add("int");
		shortlist.add("integer");
		shortlist.add("short");
		FIELD_CHANGE_RULES.put("short", shortlist);

		List<String> bytelist = new ArrayList<>();
		bytelist.add("longtext");
		bytelist.add("varchar");
		bytelist.add("text");
		bytelist.add("bigint");
		bytelist.add("int");
		bytelist.add("short");
		bytelist.add("integer");
		FIELD_CHANGE_RULES.put("byte", bytelist);

		List<String> varcharlist = new ArrayList<>();
		varcharlist.add("longtext");
		varcharlist.add("varchar");
		varcharlist.add("text");
		varcharlist.add("int");
		varcharlist.add("bigint");
		FIELD_CHANGE_RULES.put("varchar", varcharlist);

		List<String> text = new ArrayList<>();
		text.add("longtext");
		text.add("text");
		text.add("varchar");
		FIELD_CHANGE_RULES.put("text", text);

		List<String> longtextlist = new ArrayList<>();
		longtextlist.add("longtext");
		FIELD_CHANGE_RULES.put("longtext", longtextlist);
		
		List<String> bloblist = new ArrayList<>();
		bloblist.add("blob");
		FIELD_CHANGE_RULES.put("blob", bloblist);
	}

	public static List<String> compare(String tableName, List<ColumnDesc> newColumns, List<ColumnDesc> oldColumns) throws Exception {
		HashMap<String, ColumnDesc> oldColumnMap = new HashMap<>();
		List<String> ret = new ArrayList<>();
		for (ColumnDesc columnInfo : oldColumns) {
			oldColumnMap.put(columnInfo.getName(), columnInfo);
		}
		for (ColumnDesc newCol : newColumns) {
			ColumnDesc oldCol = oldColumnMap.get(newCol.getName());
			if (oldCol == null) {// 原表中 没有这一列,新增
				ret.add("ALTER TABLE `" + tableName + "` ADD COLUMN " + newCol.toDDL() + ";");
			} else if (ableChange(newCol, oldCol)) {// 修改
				String sql = getChangeSql(newCol, oldCol);
				if (!sql.equals("")) {
					ret.add("ALTER TABLE `" + tableName + "` MODIFY COLUMN " + sql + ";");
				}
			} else {
				throw new Exception(tableName + " " + newCol.toString() + " to " + oldCol + "列类型不匹配  无法自动变更");
			}
		}
		return ret;
	}

	public static String getChangeSql(ColumnDesc newColumn, ColumnDesc oldColumn) {
		
		// 长度只能往大修改，如果变小，不修改
		if (newColumn.getType().equals(oldColumn.getType()) && !sizeChanged(newColumn, oldColumn)
		        && newColumn.isAllowNull() == oldColumn.isAllowNull()) {
			return "";
		}
		return newColumn.toDDL();
	}
	
	private static boolean sizeChanged(ColumnDesc newColumn, ColumnDesc oldColumn){
		if(isNumberic(newColumn) && isNumberic(oldColumn)){
			return false;
		}
		if(newColumn.getSize() > oldColumn.getSize()){
			return true;
		} else {
			return false;
		}
		
		
	}
	
	private static boolean isNumberic(ColumnDesc column) {
		String type = column.getType();
		if (type.equalsIgnoreCase(FieldType.TINYINT.name()) || type.equalsIgnoreCase(FieldType.SMALLINT.name())
				|| type.equalsIgnoreCase(FieldType.INT.name()) || type.equalsIgnoreCase(FieldType.BIT.name())
				|| type.equalsIgnoreCase(FieldType.BIGINT.name())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否可以变更
	 * 
	 * @param newCol
	 * @param oldCol
	 * @return
	 */
	private static boolean ableChange(ColumnDesc newCol, ColumnDesc oldCol) {
		List<String> list = FIELD_CHANGE_RULES.get(newCol.getType());
		if (list == null) {
			return false;
		}
		if (!list.contains(oldCol.getType())) {
			return false;
		}
		return true;
	}
}
