package info.xiaomo.core.db.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * JDBC模板
 * @author 张力
 */
public class JdbcTemplate {

	private final static Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

	public final static RowMapper<Integer> INT_MAPPER = new IntegerRowMaper();
	public final static RowMapper<Long> LONG_MAPPER = new LongRowMapper();
	public final static RowMapper<String> STRING_MAPPER = new StringRowMapper();
	public final static RowMapper<Date> DATE_MAPPER = new DateRowMaper();
	public final static RowMapper<byte[]> BYTEARRAY_MAPPER = new ByteArrayRowMaper();
	public final static RowMapper<Map<String, Object>> MAP_MAPPER = new MapRowMapper();

	/**
	 * 数据库连接池
	 */
	private ConnectionPool pool;

	public JdbcTemplate(ConnectionPool pool) throws Exception {
		this.pool = pool;
	}

	/**
	 * 查询一条指定数据
	 * 
	 * @param sql
	 * @param mapper
	 * @param parameters
	 * @return
	 */
	public <T> T query(String sql, RowMapper<T> mapper, Object... parameters) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setObject(i + 1, parameters[i]);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return mapper.mapping(rs);
			}
		} catch (Exception e) {
			LOGGER.error("查询单条数据失败,sql:" + sql, e);
		} finally {
			release(conn, pstmt, rs, mapper);
		}
		return null;
	}

	public <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... parameters) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setObject(i + 1, parameters[i]);
			}
			rs = pstmt.executeQuery();
			List<T> ret = new ArrayList<>();
			while (rs.next()) {
				ret.add(mapper.mapping(rs));
			}
			return ret;
		} catch (Exception e) {
			LOGGER.error("查询多条数据失败,sql:" + sql, e);
		} finally {
			release(conn, pstmt, rs, mapper);
		}
		return Collections.emptyList();
	}

	public int update(String sql, Object... parameters) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < parameters.length; i++) {
				pstmt.setObject(i + 1, parameters[i]);
			}
			return pstmt.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("数据库更新失败,sql:" + sql, e);
			return -1;
		} finally {
			release(conn, pstmt, null, null);
		}
	}

	public void batchUpdate(String sql, List<Object[]> parameters) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = pool.getConnection();
			pstmt = conn.prepareStatement(sql);
			for (Object[] parameterArray : parameters) {
				for (int i = 0; i < parameterArray.length; i++) {
					pstmt.setObject(i + 1, parameterArray[i]);
				}
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (Exception e) {
			LOGGER.error("批处理更新失败,sql:" + sql, e);
		} finally {
			release(conn, pstmt, null, null);
		}
	}

	private void release(Connection conn, PreparedStatement pstmt, ResultSet rs, RowMapper<?> mapper) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.error("Result关闭出错。", e);
			}
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				LOGGER.error("PreparedStatement关闭出错。", e);
			}
		}

		if (conn != null) {
			try {
				pool.release(conn);
			} catch (SQLException e) {
				LOGGER.error("Connection关闭出错。", e);
			}
		}
		
		if(mapper != null){
			try {
				mapper.release();
			} catch (Exception e) {
				LOGGER.error("Mapper释放出错。", e);
			}
		}
	}

	private static class IntegerRowMaper implements RowMapper<Integer> {

		@Override
		public Integer mapping(ResultSet rs) throws SQLException {
			return rs.getInt(1);
		}

		@Override
		public void release() { }
		
	}

	private static class LongRowMapper implements RowMapper<Long> {

		@Override
		public Long mapping(ResultSet rs) throws SQLException {
			return rs.getLong(1);
		}
		
		@Override
		public void release() { }
	}

	private static class StringRowMapper implements RowMapper<String> {

		@Override
		public String mapping(ResultSet rs) throws SQLException {
			return rs.getString(1);
		}
		
		@Override
		public void release() { }
	}

	private static class DateRowMaper implements RowMapper<Date> {

		@Override
		public Date mapping(ResultSet rs) throws SQLException {
			return rs.getDate(1);
		}
		
		@Override
		public void release() { }
	}

	private static class ByteArrayRowMaper implements RowMapper<byte[]> {

		@Override
		public byte[] mapping(ResultSet rs) throws SQLException {
			return rs.getBytes(1);
		}
		
		@Override
		public void release() { }
	}

	private static class MapRowMapper implements RowMapper<Map<String, Object>> {
		
		private ThreadLocal<ResultSetMetaData> threadLocal = new ThreadLocal<>();
		
		@Override
		public Map<String, Object> mapping(ResultSet rs) throws SQLException {
			Map<String, Object> ret = new HashMap<>();
			ResultSetMetaData rsmd = threadLocal.get();
			if(rsmd == null){
				rsmd = rs.getMetaData();
				threadLocal.set(rsmd);
			}
			int column = rsmd.getColumnCount();
			for (int i = 1; i <= column; i++) {
				ret.put(rsmd.getColumnName(i), rs.getObject(i));
			}
			return ret;
		}
		
		@Override
		public void release() { 
			threadLocal.remove();
		}
		
	}

	public ConnectionPool getPool() {
		return pool;
	}

	public void setPool(ConnectionPool pool) {
		this.pool = pool;
	}
	
	

}
