package info.xiaomo.server.gate.script;

import info.xiaomo.gengine.common.bean.Reason;
import info.xiaomo.gengine.script.IScript;
import org.apache.mina.core.session.IoSession;

/**
 * 用户接口
 *
 * <p>2017年7月26日 下午4:42:39
 */
public interface IUserScript extends IScript {

  /**
   * 用户退出处理
   *
   * <p>2017年7月26日 下午4:47:34
   *
   * @param session 游戏客户端会话
   * @param reason 原因
   */
  default void quit(IoSession session, Reason reason) {}
}
