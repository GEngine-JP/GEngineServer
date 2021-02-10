package info.xiaomo.server.hall;

import java.io.File;
import info.xiaomo.core.persist.redis.jedis.JedisManager;
import info.xiaomo.core.script.ScriptManager;
import info.xiaomo.server.hall.manager.MongoManager;
import info.xiaomo.server.hall.server.HallServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 大厅启动类
 *
 * <p>2017年6月28日 上午11:30:49
 */
public final class AppHall {
  private static final Logger LOGGER = LoggerFactory.getLogger(AppHall.class);
  private static String configPath;
  protected static JedisManager redisManager;
  private static HallServer bydrServer;

  private AppHall() {}

  public static void main(String[] args) {
    initConfigPath();
    // redis
    redisManager = new JedisManager(configPath);
    //		RedissonManager.connectRedis(configPath);

    // 创建mongodb连接
    MongoManager.getInstance().createConnect(configPath);

    // 加载脚本
    ScriptManager.getInstance().init(str -> System.exit(0));

    // 启动通信连接
    bydrServer = new HallServer(configPath);
    new Thread(bydrServer).start();
  }

  private static void initConfigPath() {
    File file = new File(System.getProperty("user.dir"));
    if ("target".equals(file.getName())) {
      configPath = file.getPath() + File.separatorChar + "config";
    } else {
      configPath = file.getPath() + File.separatorChar + "target" + File.separatorChar + "config";
    }
    LOGGER.info("配置路径为：" + configPath);
  }

  public static HallServer getBydrServer() {
    return bydrServer;
  }
}
