package info.xiaomo.server.rpg.server.game;

import info.xiaomo.server.rpg.entify.Role;
import info.xiaomo.server.rpg.entify.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author qq
 */
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
    }

    public void unregister(Session session) {
        if (session != null) {
            User user = session.getUser();
            boolean remove = uidSessionMap.remove(user.getId(), session);
            unregisterPlayer(session, true);
            log.info("Session unregister, userId={}, remove={}", user.getId(), remove);
        }
    }

    public Session[] getSessionArray() {
        Collection<Session> values = uidSessionMap.values();
        return values.toArray(new Session[values.size()]);
    }

    public void registerPlayer(Session session) {
        Role role = session.getRole();
        if (role != null) {
            ridSessionMap.put(role.getId(), session);
        }
    }

    public void unregisterPlayer(Session session, boolean clearUid) {
        Role role = session.getRole();
        if (role != null) {
            ridSessionMap.remove(role.getId(), session);
        }
        if (clearUid) {
            session.clearAttribute();
        }
    }

    public boolean isOnline(long roleId) {
        return ridSessionMap.containsKey(roleId);
    }

    public Session[] sessionArray() {
        return ridSessionMap.values().toArray(new Session[0]);
    }
}
