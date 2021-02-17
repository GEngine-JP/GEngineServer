package info.xiaomo.server.gameserverscript.bydr;

import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.persist.redis.jedis.JedisManager;
import info.xiaomo.gengine.persist.redis.key.BydrKey;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.server.gameserver.script.IRoleScript;
import info.xiaomo.server.shared.entity.UserRole;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class RoleUtil {
    private UserRole userRole;

    public void addFishHitsCount(int configId) {
        userRole.getFishHits().merge(configId, 1, Integer::sum);
    }


    public void addFishDiesCount(int configId) {
        userRole.getFishDies().merge(configId, 1, Integer::sum);
    }


    /**
     * 修改金币
     *
     * @param gold
     * @param reason
     *
     * Role>
     * 2017年9月25日 下午5:23:41
     */
    public void changeGold(int gold, GlobalReason reason) {
        ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.changeGold(userRole, gold, reason));
    }

    /**
     * 同步金币
     *
     * @param reason
     *
     * Role>
     * 2017年9月26日 上午10:42:24
     */
    public void syncGold(GlobalReason reason) {
        ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IRoleScript.class, script -> script.syncGold(userRole, reason));
    }

    public void saveToRedis(String propertiesName) {
        if (userRole.getRoomId() < 1) {
            throw new RuntimeException(String.format("角色ID %d 异常", userRole.getRoomId()));
        }
        String key = BydrKey.Role_Map.getKey(userRole.getRoomId());
        Method method = UserRole.WRITEMETHODS.get(propertiesName);
        if (method != null) {
            try {
                Object value = method.invoke(userRole);
                if (value != null) {
                    JedisManager.getJedisCluster().hset(key, propertiesName, value.toString());
                } else {
                    log.warn("属性{}值为null", propertiesName);
                }

            } catch (Exception e) {
                log.error("属性存储", e);
            }
        } else {
            log.warn("属性：{}未找到对应方法", propertiesName);
        }
    }
}
