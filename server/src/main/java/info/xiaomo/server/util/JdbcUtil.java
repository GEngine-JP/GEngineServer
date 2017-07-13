package info.xiaomo.server.util;


import info.xiaomo.core.db.jdbc.JdbcTemplate;
import info.xiaomo.core.db.jdbc.RowMapper;

import java.util.List;

public class JdbcUtil {

	private static JdbcTemplate template;

	public static void init(JdbcTemplate template) {
		JdbcUtil.template = template;
	}

	public static JdbcTemplate getTemplate() {

		return template;
	}

	public static <T> T query(String sql, RowMapper<T> mapper, Object... parameters) {
		if (template == null) {
			throw new RuntimeException("jdbc模板类未初始化");
		}
		return template.query(sql, mapper, parameters);
	}

	public static <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... parameters) {
		if (template == null) {
			throw new RuntimeException("jdbc模板类未初始化");
		}
		return template.queryList(sql, mapper, parameters);
	}

	public static int update(String sql, Object... parameters) {
		if (template == null) {
			throw new RuntimeException("jdbc模板类未初始化");
		}

		return template.update(sql, parameters);
	}

	public static void batchUpdate(String sql, List<Object[]> parameters) {

		if (template == null) {
			throw new RuntimeException("jdbc模板类未初始化");
		}
		template.batchUpdate(sql, parameters);
	}

}
