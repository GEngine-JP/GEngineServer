package info.xiaomo.server.hall.script;

import info.xiaomo.gengine.script.IScript;
import info.xiaomo.server.gameserver.protocol.ServerMessage;

/**
 * 游戏服务器状态监测脚本
 *
 * <p>2017年7月10日 下午4:29:45
 */
public interface IGameServerCheckScript extends IScript {

  /**
   * 构建服务器状态信息
   *
   * @param builder
   */
  default void buildServerInfo(ServerMessage.ServerInfo.Builder builder) {}
}
