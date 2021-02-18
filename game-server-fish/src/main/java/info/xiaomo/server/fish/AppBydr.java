package info.xiaomo.server.fish;

import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.server.fish.manager.MongoManager;
import info.xiaomo.server.fish.server.BydrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 捕鱼达人启动类
 * <p>
 * <p>
 * Role> 2017年6月28日 上午11:30:49
 */
public final class AppBydr {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppBydr.class);
    private static String configPath;
    protected static JedisManager redisManager;
    private static BydrServer bydrServer;

    private AppBydr() {
    }

    public static void main(String[] args) {
        initConfigPath();
        // redis
        redisManager = new JedisManager(configPath);
        redisManager.initScript(configPath);
        JedisManager.setRedisManager(redisManager);


        // 创建mongodb连接
        MongoManager.getInstance().createConnect(configPath);

        // 加载脚本
//        ScriptManager.getInstance().init(str -> System.exit(0));

        // 启动通信连接
        bydrServer = new BydrServer(configPath);
        new Thread(bydrServer).start();
    }

    private static void initConfigPath() {
        configPath = System.getProperty("user.dir") + "/game-conf";
        LOGGER.info("配置路径为：" + configPath);
    }

    public static BydrServer getBydrServer() {
        return bydrServer;
    }

    public static String getConfigPath() {
        return configPath;
    }
}
