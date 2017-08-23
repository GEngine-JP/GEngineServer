package info.xiaomo.server.util;


import info.xiaomo.gameCore.persist.jdbc.JdbcTemplate;
import info.xiaomo.server.server.GameContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 田小军 on 2017/5/8 0008.
 */
public class DruidDBPoolManager {
    private static final ConcurrentHashMap<Integer, JdbcTemplate> pools = new ConcurrentHashMap<>();

    public static JdbcTemplate get(int serverId) {
        if (serverId == 0) {
            serverId = GameContext.getOption().getServerId();
        }
        return pools.get(serverId);
    }

    public static void add(int serverId, JdbcTemplate jdbcTemplate) {
        pools.put(serverId, jdbcTemplate);
    }

    public static ConcurrentHashMap<Integer, JdbcTemplate> getPools() {
        return pools;
    }
}
