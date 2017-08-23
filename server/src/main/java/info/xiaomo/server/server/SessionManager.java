package info.xiaomo.server.server;

import info.xiaomo.server.entify.Player;
import info.xiaomo.server.entify.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    private SessionManager() {

    }

    private final ConcurrentMap<Long, Session> uidSessionMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Session> ridSessionMap = new ConcurrentHashMap<>();

    public Session getSession(long uid) {
        return uidSessionMap.get(uid);
    }

    public Session getSessionByRoleId(long rid) {
        return ridSessionMap.get(rid);
    }

    public void register(Session session) {
        uidSessionMap.put(session.getUser().getId(), session);
//        OffLineRoleManager.getInstance().playerOnline(session.getUser().getId());
    }

    public void unregister(Session session) {
        if (session != null) {
            User user = session.getUser();
            boolean remove = uidSessionMap.remove(user.getId(), session);
            unregisterPlayer(session, true);
            log.info("Session unregister, userId={}, remove={}", user.getId(), remove);
//            OffLineRoleManager.getInstance().playerOffline(user.getId());
        }
    }

    public Session[] getSessionArray() {
        Collection<Session> values = uidSessionMap.values();
        return values.toArray(new Session[values.size()]);
    }

    public void registerPlayer(Session session) {
        Player player = session.getPlayer();
        if (player != null) {
            ridSessionMap.put(player.getId(), session);
        }
    }

    public void unregisterPlayer(Session session, boolean clearUid) {
        Player player = session.getPlayer();
        if (player != null) {
            ridSessionMap.remove(player.getId(), session);
        }
        if (clearUid) {
            session.clearAttribute();
        }
    }

    public boolean isOnline(long roleId) {
        return ridSessionMap.containsKey(roleId);
    }

    public Session[] sessionArray(){
        return ridSessionMap.values().toArray(new Session[0]);
    }
}
