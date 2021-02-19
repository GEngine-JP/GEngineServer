package info.xiaomo.server.fish.test;

import info.xiaomo.gengine.persist.redis.jedis.JedisClusterConfig;
import info.xiaomo.gengine.utils.PathUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.shared.config.FishGameConfig;
import org.junit.Test;

public class ConfigTest {

    /**
     * 测试获取项目根路径
     */
    @Test
    public void testConfigUrl() {
        System.out.println(PathUtil.getConfigPath());
    }

    /**
     * 测试yamlUtil读写正确性
     */
    @Test
    public void testYamlReader() {
        String path = PathUtil.getConfigPath();
        String file = "jedisClusterConfig.yml";
        JedisClusterConfig jedisClusterConfig = YamlUtil.read(path + file, JedisClusterConfig.class);
        System.out.println(jedisClusterConfig.getNodes().size());

        file = "gameConfig.yml";
        FishGameConfig fishGameConfig = YamlUtil.read(path + file, FishGameConfig.class);
        System.out.println(fishGameConfig.getThreadRoomNum());

    }


}
