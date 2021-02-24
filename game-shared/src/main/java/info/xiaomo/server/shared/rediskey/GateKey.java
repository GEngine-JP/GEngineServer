package info.xiaomo.server.shared.rediskey;

/**
 * 网关 redis key枚举
 *
 * <p>2017年7月25日 上午11:39:16
 */
public enum GateKey {
    /** 服务器启动时间 */
    GM_Gate_StartTime("GM_%d:Hall:starttime"),
    ;

    private final String key;

    GateKey(String key) {
        this.key = key;
    }

    public String getKey(Object... objects) {
        return String.format(key, objects);
    }
}
