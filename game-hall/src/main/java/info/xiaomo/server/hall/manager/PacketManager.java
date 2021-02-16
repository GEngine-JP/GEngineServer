package info.xiaomo.server.hall.manager;

import java.util.function.Consumer;
import info.xiaomo.gengine.common.bean.Reason;
import info.xiaomo.gengine.common.struct.GameItem;
import info.xiaomo.gengine.common.struct.Role;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.server.hall.script.IPacketScript;
import info.xiaomo.server.protocol.hall.HallPacketMessage.PacketItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 背包
 *
 * <p>2017年9月18日 下午2:49:17
 */
public class PacketManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(PacketManager.class);
  private static volatile PacketManager packetManager;

  private PacketManager() {}

  public static PacketManager getInstance() {
    if (packetManager == null) {
      synchronized (PacketManager.class) {
        if (packetManager == null) {
          packetManager = new PacketManager();
        }
      }
    }
    return packetManager;
  }

  /**
   * 使用道具
   *
   * <p>2017年9月18日 下午4:25:54
   *
   * @param id
   * @param num
   * @param reason
   * @param itemConsumer
   */
  public void useItem(Role role, long id, int num, Reason reason, Consumer<GameItem> itemConsumer) {
    ScriptManager.getInstance()
        .getBaseScriptEntry()
        .executeScripts(
            IPacketScript.class, script -> script.useItem(role, id, num, reason, itemConsumer));
  }

  /**
   * 添加道具
   *
   * <p>2017年9月18日 下午4:23:47
   *
   * @param configId
   * @param num 数量
   * @param reason
   * @param itemConsumer
   */
  public GameItem addItem(
      Role role, int configId, int num, Reason reason, Consumer<GameItem> itemConsumer) {
    return ScriptManager.getInstance()
        .getBaseScriptEntry()
        .functionScripts(
            IPacketScript.class,
            (IPacketScript script) -> script.addItem(role, configId, num, reason, itemConsumer));
  }

  /**
   * 构建
   *
   * <p>2017年9月18日 下午4:07:49
   *
   * @param gameItem
   * @return
   */
  public PacketItem buildPacketItem(GameItem gameItem) {
    PacketItem.Builder builder = PacketItem.newBuilder();
    builder.setId(gameItem.getId());
    builder.setConfigId(gameItem.getConfigId());
    builder.setNum(gameItem.getNum());
    return builder.build();
  }

  /**
   * 获取物品
   *
   * <p>2017年9月18日 下午5:10:36
   *
   * @param itemId
   * @return
   */
  public GameItem getItem(Role role, long itemId) {
    return role.getItem(itemId);
  }
}
