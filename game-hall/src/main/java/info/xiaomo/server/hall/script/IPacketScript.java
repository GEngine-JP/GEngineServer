package info.xiaomo.server.hall.script;

import java.util.function.Consumer;
import info.xiaomo.core.common.bean.Reason;
import info.xiaomo.core.common.struct.GameItem;
import info.xiaomo.core.common.struct.Role;
import info.xiaomo.core.script.IScript;

/**
 * 道具
 *
 * <p>2017年9月18日 下午4:18:44
 */
public interface IPacketScript extends IScript {

  /**
   * 使用道具
   *
   * <p>2017年9月18日 下午4:20:16
   *
   * @param id 道具Id
   * @param num 数量
   * @param itemConsumer
   */
  default void useItem(Role role, long id, int num, Reason reason, Consumer<GameItem> itemConsumer) {}

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
  default GameItem addItem(
      Role role, int configId, int num, Reason reason, Consumer<GameItem> itemConsumer) {
    return null;
  }
}
