package info.xiaomo.core.db.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接池
 * @author 张力
 */
public interface ConnectionPool {
	

	/**
	 * 获取一个数据库连接
	 * 
	 *            连接池名称
	 * @return Connection
	 * @throws SQLException  SQLException
	 */
	Connection getConnection() throws SQLException;

	/**
	 * 释放一个数据库连接
	 * 
	 * @param connection
	 *            连接
	 */
	void release(Connection connection) throws SQLException;


}
