package info.xiaomo.core.log;

import java.lang.reflect.Method;

/**
 * 日志表的列描述
 * @author 张力
 *
 */
class ColumnDesc {
	
	private Method readMethod;

	/**
	 * 列名
	 */
	private String name;
	/**
	 * 数据库类型
	 */
	private String type;
	/**
	 * 长度
	 */
	private int size;
	/**
	 * 是否允许为空
	 */
	private boolean allowNull;

	/**
	 * 自动增长
	 */
	private boolean autoIncrement;
	
	/**
	 * 注释
	 */
	private String commit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}
	

	public String getCommit() {
		return commit;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	private String getFieldType() {
		if (this.type.equalsIgnoreCase("text")
				|| this.type.equalsIgnoreCase("longtext") || this.type.equalsIgnoreCase("blob")) {
			return this.type;
		}
		return this.type + "(" + this.size + ")";
	}

	private String getNullable() {
		return this.allowNull ? "" : "\tnot null";
	}

	public String toDDL() {
		String ddl = "`" + this.name + "`" + "\t" + this.getFieldType()
				+ this.getNullable() + this.getAutoIncrementable() +this.getCommitStr();
		return ddl;
	}

	private String getAutoIncrementable() {
		return this.autoIncrement ? "\tAUTO_INCREMENT" : "";
	}
	
	private String getCommitStr(){
		return this.commit.equals("") ? "" : "\tCOMMENT '" + this.commit+"'";
	}

}
