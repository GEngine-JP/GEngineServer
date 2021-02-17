package info.xiaomo.server.gameserver.gateway.script;

import info.xiaomo.gengine.network.server.ServerType;
import info.xiaomo.gengine.script.IScript;
import org.apache.mina.filter.firewall.BlacklistFilter;

/**
 * 服务器脚本
 *
 *   2017年9月1日 下午2:37:33
 */
public interface IGateServerScript extends IScript {

  /**
   * 是否为udp消息
   *
   *   2017年9月1日 下午2:40:01
   * @param serverType 判断游戏类型是否支持udp
   * @param msgId 消息ID
   * @return
   */
  default boolean isUdpMsg(ServerType serverType, int msgId) {
    return false;
  }

  /**
   * 设置IP黑名单
   *
   *   2017年9月4日 上午11:23:21
   * @param filter
   */
  default void setIpBlackList(BlacklistFilter filter) {}
}
