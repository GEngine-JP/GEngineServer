package info.xiaomo.core.db.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库行转换器
 * @author 张力
 */
public interface RowMapper<T> {

	T mapping(ResultSet rs) throws SQLException;
	
	void release();
	
	
	class IntegerRowMaper implements RowMapper<Integer> {

		@Override
		public Integer mapping(ResultSet rs) throws SQLException {
			return rs.getInt(1);
		}

		@Override
		public void release() { }
		
	}

	class LongRowMapper implements RowMapper<Long> {

		@Override
		public Long mapping(ResultSet rs) throws SQLException {
			return rs.getLong(1);
		}
		
		@Override
		public void release() { }
	}

	class StringRowMapper implements RowMapper<String> {

		@Override
		public String mapping(ResultSet rs) throws SQLException {
			return rs.getString(1);
		}
		
		@Override
		public void release() { }
	}

	class DateRowMaper implements RowMapper<Date> {

		@Override
		public Date mapping(ResultSet rs) throws SQLException {
			return rs.getDate(1);
		}
		
		@Override
		public void release() { }
	}

	class ByteArrayRowMaper implements RowMapper<byte[]> {

		@Override
		public byte[] mapping(ResultSet rs) throws SQLException {
			return rs.getBytes(1);
		}
		
		@Override
		public void release() { }
	}

	class MapRowMapper implements RowMapper<Map<String, Object>> {
		
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
}
