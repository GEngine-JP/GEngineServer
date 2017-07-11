package info.xiaomo.core.log;

import info.xiaomo.core.log.annotation.Column;
import info.xiaomo.core.log.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * 日志基类
 * 
 * @author 张力
 */
@Table(cycle = TableCycle.SINGLE, primaryKey = "id")
public abstract class AbstractLog implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractLog.class);

	private final static Map<Class<?>, TableDesc> tableDescMap = new HashMap<>();

	void init() throws Exception {

		Class<?> clazz = this.getClass();
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			return;
		}
		TableDesc desc = new TableDesc();
		desc.setCycle(table.cycle());
		String tableName = table.tableName();
		if (tableName.equals("")) {
			tableName = clazz.getSimpleName();
		}
		desc.setName(tableName);
		desc.setPrimaryKey(table.primaryKey());

		// 找出所有父类，父类的属性先遍历，所以将顶级父类放在了列表第一个
		List<Class<?>> clazzList = new ArrayList<>();
		while (clazz.getSuperclass() != null && clazz != Object.class) {
			clazzList.add(0, clazz);
			clazz = clazz.getSuperclass();
		}

		// 遍历所有类（包括父类），获取日志属性
		for (Class<?> cl : clazzList) {
			Field[] fields = cl.getDeclaredFields();
			for (Field field : fields) {
				Column column = field.getAnnotation(Column.class);
				if (column == null) {
					continue;
					// 全部使用默认值
				}

				PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cl);
				Method readMethod = pd.getReadMethod();
				if (readMethod == null) {
					continue;
				}
				ColumnDesc colDesc = new ColumnDesc();
				colDesc.setAllowNull(column.allowNull());
				colDesc.setAutoIncrement(column.autoIncrement());
				String colName = column.colName();
				if (colName.equals("")) {
					colName = field.getName();
				}
				colDesc.setName(colName.toLowerCase());
				colDesc.setReadMethod(readMethod);
				colDesc.setSize(column.size());
				colDesc.setType(column.fieldType().name().toLowerCase());
				colDesc.setCommit(column.commit());
				desc.addCol(colDesc);
			}
		}
		desc.init();
		tableDescMap.put(this.getClass(), desc);
		checkTable();
	}

	private String buildCreateSql() {
		return String.format(tableDescMap.get(this.getClass()).getCreateSql(),
		        tableDescMap.get(this.getClass()).buildName(System.currentTimeMillis()));
	}

	String buildInsertSQL() {
		return String.format(tableDescMap.get(this.getClass()).getInsertSql(),
		        tableDescMap.get(this.getClass()).buildName(System.currentTimeMillis()));
	}

	Object[] buildInsertParam() {
		return tableDescMap.get(this.getClass()).buildInsertParam(this);
	}

	private void checkTable() throws Exception {
		String buildName = tableDescMap.get(this.getClass()).buildName(System.currentTimeMillis());
		LOGGER.info("检测查表" + buildName);
		Connection connection = null;
		Statement statement = null;
		try {
			connection = LogService.template.getPool().getConnection();
			List<String> tableNames = getTableName(connection);
			if (tableNames.contains(buildName)) {
				// 原有表结构
				List<ColumnDesc> columnDefine = getColumnDefine(connection, buildName);
				// 表的主键
				List<String> primaryKeys = getTablePrimaryKeys(connection, buildName);
				// 去除主键
				// 主键不进行变更
				columnDefine.removeIf(next -> primaryKeys.contains(next.getName()));

				// 去除主键
				List<ColumnDesc> newColumns = new ArrayList<>();
				for (ColumnDesc col : tableDescMap.get(this.getClass()).getColumns()) {
					if (!tableDescMap.get(this.getClass()).getPrimaryKey().equals(col.getName())) {
						newColumns.add(col);
					}
				}

				List<String> compartor = TableCompareUtil.compare(buildName, newColumns, columnDefine);
				if (compartor.size() > 0) {
					statement = connection.createStatement();
					for (String string : compartor) {
						LOGGER.info("检查到变更：" + string);
						statement.addBatch(string);
					}
					statement.executeBatch();
				}
			} else {
				// 当前数据库没有这个表
				if (tableDescMap.get(this.getClass()).getCycle() == TableCycle.SINGLE) {// 不按时间周期滚动的表，预先创建
					String createSql = this.buildCreateSql();
					try {
						LogService.template.update(createSql);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			LOGGER.info(buildName + "检查结束");
		}finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					LOGGER.error("关闭statement失败", e);
				}
			}
			if (connection != null) {
				LogService.template.getPool().release(connection);
			}
		}

	}

	private List<ColumnDesc> getColumnDefine(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
		List<ColumnDesc> infos = new ArrayList<>();
		while (columns.next()) {
			ColumnDesc info = new ColumnDesc();
			info.setName(columns.getString("COLUMN_NAME").toLowerCase());
			info.setType(columns.getString("TYPE_NAME").toLowerCase());
			info.setSize(columns.getInt("COLUMN_SIZE"));
			info.setAllowNull(columns.getBoolean("IS_NULLABLE"));
			infos.add(info);
			//LOGGER.error("table:" + tableName + ",type:" + info.getType() + ",name:" + info.getName() + ",size:" + info.getSize());
		}
		

		return infos;
	}

	private List<String> getTablePrimaryKeys(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getPrimaryKeys(null, "%", tableName);
		List<String> ret = new ArrayList<>();
		while (rs.next()) {
			ret.add(rs.getString("COLUMN_NAME"));
		}

		return ret;
	}

	private List<String> getTableName(Connection conn) throws SQLException {
		ResultSet tableRet = conn.getMetaData().getTables(null, null, null, null);
		List<String> tableNames = new ArrayList<>();
		while (tableRet.next()) {
			tableNames.add(tableRet.getString("TABLE_NAME"));
		}
		return tableNames;
	}

	@Override
	public void run() {
		String insertSql = this.buildInsertSQL();
		Object[] params = this.buildInsertParam();
		try {
			// 插入
			int ret = LogService.template.update(insertSql, params);
			if(ret < 0) {
				// 建表
				String createSql = this.buildCreateSql();
				LogService.template.update(createSql);
				LogService.template.update(insertSql, params);
			}
		} catch (Exception e) {
			LOGGER.error("日志入库执行失败sql:" + insertSql + ",params:" + Arrays.toString(params), e);
		}
	}
}
