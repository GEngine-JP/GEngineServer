package info.xiaomo.server.util;


import info.xiaomo.gameCore.protocol.Message;
import info.xiaomo.server.server.Session;
import info.xiaomo.server.server.SessionManager;

import java.util.Collection;

public class MessageUtil {

    public static void sendMsg(Message msg, long id) {
        Session session = SessionManager.getInstance().getSession(id);
        if (session == null) {
            return;
        }
        session.sendMessage(msg);
    }

    public static void sendMsg(Session session, Message msg) {
        session.sendMessage(msg);
    }

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

    public static void sendMsgToRids(Message msg, Collection<Long> rids, Long exceptRoleId) {
        for (Long rid : rids) {
            if (rid != null && (!rid.equals(exceptRoleId))) {
                sendMsg(msg, rid);
            }
        }
    }


}
