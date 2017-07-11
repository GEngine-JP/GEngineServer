package info.xiaomo.core.log;


import info.xiaomo.core.log.annotation.Column;
import info.xiaomo.core.log.annotation.Table;

@Table(tableName="item_log",primaryKey="id",cycle=TableCycle.DAY)
public class ItemLog extends AbstractLog {
	
	@Column(allowNull = false, fieldType = FieldType.INT, size = 11, commit = "日志ID", colName = "id", autoIncrement = true)
	private int id;

	@Column(fieldType = FieldType.INT, size = 11, commit="服务器id")
	private int serverId;

	@Column(fieldType = FieldType.VARCHAR, size = 64, commit="道具名称")
	private String name;

	@Column(fieldType = FieldType.INT, size = 11, commit="时间")
	private long time;

	@Column(fieldType = FieldType.INT, size = 11, commit="道具ID")
	private int itemId;

	@Column(fieldType = FieldType.INT, size = 11, commit="数量")
	private int num;

	@Column(fieldType = FieldType.VARCHAR, size = 256, allowNull=true, commit="触发动作")
	private String action;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
