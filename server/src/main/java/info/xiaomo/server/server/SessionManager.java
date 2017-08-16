package info.xiaomo.server.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	
	private static final SessionManager INSTANCE = new SessionManager();
	
	private Map<Long, UserSession> userSessionMap = new ConcurrentHashMap<>();

	private Map<Channel, UserSession> channelSessionMap = new ConcurrentHashMap<>();

	public static SessionManager getInstance(){
		return INSTANCE;
	}
	
	public UserSession getSession(long uid) {
		return userSessionMap.get(uid);
	}
	
	public void register(long uid, UserSession session) {
		userSessionMap.put(uid, session);
	}
	
	public UserSession[] sessionArray(){
		return userSessionMap.values().toArray(new UserSession[0]);
	}
	
	
}
