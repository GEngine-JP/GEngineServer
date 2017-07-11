package info.xiaomo.core.db.persist;

/**
 * 
 * 数据持久化构造器
 * @author 张力
 *
 */
public interface PersistFactory {
	
	String name();
	
	int dataType();
	
	String createInsertSql();
	String createUpdateSql();
	String createDeleteSql();
	
	Object[] createInsertParameters(Persistable obj);
	Object[] createUpdateParameters(Persistable obj);
	Object[] createDeleteParameters(Persistable obj);
	
	long taskPeriod();
	
}
