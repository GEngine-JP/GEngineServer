package info.xiaomo.server.rpg.util;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Message;
import java.util.Collection;
import info.xiaomo.server.rpg.server.game.Session;
import info.xiaomo.server.rpg.server.game.SessionManager;
import lombok.extern.slf4j.Slf4j;

/** @author xiaomo */
@Slf4j
public class MessageUtil {

    public static void sendMsgToRids(Message msg, long... rids) {
        for (long rid : rids) {
            sendMsg(msg, rid);
        }
    }

    public static void sendMsgToRids(Message msg, Collection<Long> rids) {
        for (Long rid : rids) {
            if (rid != null) {
                sendMsg(msg, rid);
            }
        }
    }

    public static void sendMsgToRids(
            AbstractMessage msg, Collection<Long> rids, Long exceptRoleId) {
        for (Long rid : rids) {
            if (rid != null && (!rid.equals(exceptRoleId))) {
                sendMsg(msg, rid);
            }
        }
    }

    public static void sendMsg(Message msg, long id) {
        Session session = SessionManager.getInstance().getSession(id);
        if (session == null) {
            return;
        }
        sendMsg(session, msg);
    }

    public static void sendMsg(Session session, Message msg) {
        session.sendMessage(msg);
    }
}
