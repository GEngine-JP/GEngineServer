package info.xiaomo.server.gateway.script;

import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.IScript;
import org.apache.mina.core.session.IoSession;

/**
 * 用户接口
 *
 * 2017年7月26日 下午4:42:39
 */
public interface IUserScript extends IScript {
	void quit(IoSession session, GlobalReason sessionIdle);

	/**
	 * 用户退出处理
	 *
	 * 2017年7月26日 下午4:47:34
	 *
	 * @param session 游戏客户端会话
	 * @param reason 原因
	 */
}
