package info.xiaomo.server.fish.test;

import info.xiaomo.gengine.utils.ConfigUtil;
import info.xiaomo.gengine.utils.YamlUtil;
import info.xiaomo.server.shared.config.FishGameConfig;
import info.xiaomo.server.shared.config.JedisClusterConfig;
import org.junit.Test;

public class ConfigTest {

	/**
	 * 测试获取项目根路径
	 */
	@Test
	public void testConfigUrl() {
		System.out.println(ConfigUtil.getConfigPath());
	}

	/**
	 * 测试yamlUtil读写正确性
	 */
	@Test
	public void testYamlReader() {
		String path = ConfigUtil.getConfigPath();
		String file = "jedisClusterConfig.yml";
		JedisClusterConfig jedisClusterConfig = YamlUtil.read(path + file, JedisClusterConfig.class);
		System.out.println(jedisClusterConfig.getNodes().size());

		file = "gameConfig.yml";
		FishGameConfig fishGameConfig = YamlUtil.read(path + file, FishGameConfig.class);
		System.out.println(fishGameConfig.getThreadRoomNum());

	}


}
