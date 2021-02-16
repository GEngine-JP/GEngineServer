package info.xiaomo.server.util;



import java.sql.SQLException;
import java.util.List;
import info.xiaomo.gengine.persist.mysql.jdbc.JdbcTemplate;
import info.xiaomo.gengine.persist.mysql.jdbc.RowMapper;

/**
 * @author xiaomo
 */
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


    /**
     * 该表是否存在
     *
     * @param tableName tableName
     * @return boolean
     */
    public static boolean hasTable(String tableName) {
        try {
            return template.hasTable(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createTable(String sql) {
        if (template == null) {
            throw new RuntimeException("jdbc模板类未初始化");
        }
        template.createTable(sql);
    }
}
