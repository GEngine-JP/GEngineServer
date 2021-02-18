package info.xiaomo.server.fishscript.script.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import info.xiaomo.gengine.script.IConfigScript;
import info.xiaomo.server.fish.dao.CFishDao;
import info.xiaomo.server.fish.manager.ConfigManager;
import info.xiaomo.server.shared.entity.CFish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载配置脚本
 * 
 *
 *  2017年10月18日 下午3:21:41
 */
public class LoadConfigScript implements IConfigScript {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadConfigScript.class);

	@Override
	public String reloadConfig(List<String> tables) {
		StringBuffer sb = new StringBuffer();
		synchronized (this) {
			try {
				// 鱼配置
				if (containTable(tables, CFish.class)) {
					Map<Integer, CFish> fishMap = new ConcurrentHashMap<>();
					CFishDao.getAll().forEach(fish -> {
						fishMap.put(fish.getId(), fish);
					});
					ConfigManager.getInstance().setFishMap(fishMap);
					sb.append("CFish:").append(fishMap.size());
				}

				// TODO 其他配置

			} catch (Exception e) {
				LOGGER.error("加载配置", e);
			}

		}
		LOGGER.info(sb.toString());
		return sb.toString();
	}
}
