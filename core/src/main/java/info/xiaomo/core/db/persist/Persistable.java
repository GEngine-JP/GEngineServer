package info.xiaomo.core.db.persist;

/**
 * 可持久化的数据接口
 * @author 张力
 *
 */
public interface Persistable extends CacheAble {
	
	boolean isDirty();
	
	void setDirty(boolean dirty);
	
}
