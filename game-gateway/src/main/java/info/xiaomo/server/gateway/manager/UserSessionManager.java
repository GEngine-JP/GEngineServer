package info.xiaomo.server.gateway.manager;

import com.google.protobuf.Message;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import info.xiaomo.gengine.bean.GlobalReason;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.server.gateway.script.IUserScript;
import info.xiaomo.server.gateway.struct.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户连接会话管理类
 * 
 *
 *   下午3:58:31
 */
public class UserSessionManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionManager.class);
	private static volatile UserSessionManager userSessionManager;

	/** 用户session rediskey：sessionID */
	private final Map<Long, UserSession> allSessions = new ConcurrentHashMap<>();

	/** 用户session rediskey：userID */
	private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

	/** 用户session rediskey：roleID */
	private final Map<Long, UserSession> roleSessions = new ConcurrentHashMap<>();

	private UserSessionManager() {
    }

	public static UserSessionManager getInstance() {
		if (userSessionManager == null) {
			synchronized (UserSessionManager.class) {
				if (userSessionManager == null) {
					userSessionManager = new UserSessionManager();
				}
			}
		}
		return userSessionManager;
	}

	/**
	 * 用户连接服务器
	 * 
	 * @param userSession
	 */
	public void onUserConnected(UserSession userSession) {
		if (userSession.getClientSession() != null) {
			allSessions.put(userSession.getClientSession().getId(), userSession);
		}
	}

	/**
	 * 登录大厅
	 * 
	 * @param roleId
	 */
	public void loginHallSuccess(UserSession userSession, long userId, long roleId) {
		userSession.setUserId(userId);
		userSession.setRoleId(roleId);
		// session.setAttribute(GateTcpUserServerHandler.USER_SESSION,
		// userSession);
		userSessions.put(userId, userSession);
		roleSessions.put(roleId, userSession);
	}

	public UserSession getUserSessionByUserId(long userId) {
		return userSessions.get(userId);
	}

	public UserSession getUserSessionbyRoleId(long roleId) {
		return roleSessions.get(roleId);
	}

	/**
	 * 用户session
	 * 
	 *
	 *  2017年7月21日 下午1:49:17
	 * @param sessionId
	 * @return
	 */
	public UserSession getUserSessionBySessionId(long sessionId) {
		return allSessions.get(sessionId);
	}

	public void quit(UserSession userSession, GlobalReason reason) {
		allSessions.remove(userSession.getClientSession().getId());
		userSessions.remove(userSession.getUserId());
		roleSessions.remove(userSession.getRoleId());
	}
	
	/**
	 * 广播消息给前端客户端
	 *
	 *
	 * 2017年9月13日 下午3:58:17
	 * @param msg
	 */
	public void  broadcast(Message msg) {
        allSessions.values().forEach(session ->session.sendToClient(msg));
	}
	
	/**
	 * 所有在线人数
	 *
	 *
	 * 2017年10月12日 下午1:36:58
	 * @return
	 */
	public int getOlineCount() {
		return allSessions.size();
	}
	
	/**
	 * 服务器关闭
	 *
	 *
	 * 2017年10月24日 下午1:49:14
	 */
	public void onShutdown() {
		//踢出玩家
		for(UserSession userSession:allSessions.values()) {
			ScriptManager.getInstance().getBaseScriptEntry().executeScripts(IUserScript.class, script->script.quit(userSession.getClientSession(), GlobalReason.ServerClose));
		}
	}
	
}
