package info.xiaomo.server.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	
	private static final SessionManager INSTANCE = new SessionManager();
	
	private Map<Long, Session> userSessionMap = new ConcurrentHashMap<>();

	private Map<Channel, Session> channelSessionMap = new ConcurrentHashMap<>();

	
	public static SessionManager getInstance(){
		return INSTANCE;
	}
	
	public Session getSession(long uid) {
		return userSessionMap.get(uid);
	}
	
	public void register(long uid, Session session) {
		userSessionMap.put(uid, session);
	}
	
	public Session[] sessionArray(){
		return userSessionMap.values().toArray(new Session[0]);
	}
	
	
}
